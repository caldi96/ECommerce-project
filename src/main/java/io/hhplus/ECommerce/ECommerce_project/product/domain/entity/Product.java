package io.hhplus.ECommerce.ECommerce_project.product.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ProductException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 정적 팩토리 메서드를 위한 private 생성자
public class Product {

    private Long id;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "category_id")
    // private Category category;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private boolean isActive;
    private boolean isOutOfStock;
    private int viewCount;
    private int soldCount;
    private Integer minOrderQuantity;
    private Integer maxOrderQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;  // 논리적 삭제용

    // ===== 정적 팩토리 메서드 =====

    /**
     * 상품 생성
     */
    public static Product createProduct(
        String name,
        Long categoryId,
        String description,
        BigDecimal price,
        int stock,
        Integer minOrderQuantity,
        Integer maxOrderQuantity
    ) {
        validateName(name);
        validatePrice(price);
        validateStock(stock);

        if (minOrderQuantity != null && minOrderQuantity < 1) {
            throw new ProductException(ErrorCode.PRODUCT_MIN_ORDER_QUANTITY_INVALID);
        }

        if (maxOrderQuantity != null && maxOrderQuantity < 1) {
            throw new ProductException(ErrorCode.PRODUCT_MAX_ORDER_QUANTITY_INVALID);
        }

        if (minOrderQuantity != null && maxOrderQuantity != null && minOrderQuantity > maxOrderQuantity) {
            throw new ProductException(ErrorCode.PRODUCT_MIN_ORDER_QUANTITY_EXCEEDS_MAX);
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isSoldOut = stock == 0;

        return new Product(
            null,       // id는 저장 시 생성
            categoryId,
            name,
            description,
            price,
            stock,
            true,       // isActive (초기 상태는 활성)
            isSoldOut,
            0,          // viewCount
            0,          // soldCount
            minOrderQuantity,
            maxOrderQuantity,
            now,        // createdAt
            now,        // updatedAt
            null        // deletedAt (삭제되지 않음)
        );
    }


    // ===== 비즈니스 로직 메서드 =====

    /**
     * 재고 차감 (상품 구매 시)
     */
    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new ProductException(ErrorCode.PRODUCT_DECREASE_QUANTITY_INVALID);
        }

        if (this.stock < quantity) {
            throw new ProductException(ErrorCode.PRODUCT_OUT_OF_STOCK,
                "재고가 부족합니다. 현재 재고: " + this.stock + ", 요청 수량: " + quantity);
        }

        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();

        // 재고가 0이 되면 자동으로 품절 처리
        if (this.stock == 0) {
            this.isOutOfStock = true;
        }
    }

    /**
     * 재고 증가 (주문 취소, 반품 시)
     */
    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new ProductException(ErrorCode.PRODUCT_INCREASE_QUANTITY_INVALID);
        }

        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();

        // 재고가 증가하면 품절 상태 해제
        if (this.stock > 0 && this.isOutOfStock) {
            this.isOutOfStock = false;
        }
    }

    /**
     * 재고 직접 설정 (재고 관리)
     */
    public void updateStock(int stock) {
        validateStock(stock);

        this.stock = stock;
        this.updatedAt = LocalDateTime.now();

        // 재고에 따라 품절 상태 자동 설정
        this.isOutOfStock = (stock == 0);
    }

    /**
     * 판매량 증가
     */
    public void increaseSoldCount(int quantity) {
        if (quantity <= 0) {
            throw new ProductException(ErrorCode.PRODUCT_INCREASE_SOLD_COUNT_INVALID);
        }

        this.soldCount += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 판매량 감소 (주문 취소 시)
     */
    public void decreaseSoldCount(int quantity) {
        if (quantity <= 0) {
            throw new ProductException(ErrorCode.PRODUCT_DECREASE_SOLD_COUNT_INVALID);
        }

        if (this.soldCount < quantity) {
            throw new ProductException(ErrorCode.PRODUCT_SOLD_COUNT_LESS_THAN_CANCEL,
                "판매량이 취소량보다 작습니다. 현재 판매량: " + this.soldCount + ", 취소량: " + quantity);
        }

        this.soldCount -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 조회수 증가
     */
    public void increaseViewCount() {
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 가격 수정
     */
    public void updatePrice(BigDecimal price) {
        validatePrice(price);

        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 상품명 수정
     */
    public void updateName(String name) {
        validateName(name);

        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 상품 설명 수정
     */
    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 카테고리 수정
     */
    public void updateCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 최소 주문량 수정
     */
    public void updateMinOrderQuantity(Integer minOrderQuantity) {
        if (minOrderQuantity != null && minOrderQuantity < 1) {
            throw new ProductException(ErrorCode.PRODUCT_MIN_ORDER_QUANTITY_INVALID);
        }

        if (minOrderQuantity != null && this.maxOrderQuantity != null && minOrderQuantity > this.maxOrderQuantity) {
            throw new ProductException(ErrorCode.PRODUCT_MIN_ORDER_QUANTITY_EXCEEDS_MAX);
        }

        this.minOrderQuantity = minOrderQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 최대 주문량 수정
     */
    public void updateMaxOrderQuantity(Integer maxOrderQuantity) {
        if (maxOrderQuantity != null && maxOrderQuantity < 1) {
            throw new ProductException(ErrorCode.PRODUCT_MAX_ORDER_QUANTITY_INVALID);
        }

        if (maxOrderQuantity != null && this.minOrderQuantity != null && maxOrderQuantity < this.minOrderQuantity) {
            throw new ProductException(ErrorCode.PRODUCT_MAX_ORDER_QUANTITY_LESS_THAN_MIN);
        }

        this.maxOrderQuantity = maxOrderQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 상품 활성화
     */
    public void activate() {
        if (this.isActive) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_ACTIVE);
        }

        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 상품 비활성화
     */
    public void deactivate() {
        if (!this.isActive) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_INACTIVE);
        }

        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 품절 처리 (수동)
     */
    public void outOfStock() {
        if (this.isOutOfStock) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_SOLD_OUT);
        }

        this.isOutOfStock = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 품절 해제 (수동)
     */
    public void backInStock() {
        if (!this.isOutOfStock) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_AVAILABLE);
        }

        if (this.stock == 0) {
            throw new ProductException(ErrorCode.PRODUCT_CANNOT_BE_AVAILABLE_WITH_ZERO_STOCK);
        }

        this.isOutOfStock = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 상품 삭제 (논리적 삭제)
     */
    public void delete() {
        if (this.deletedAt != null) {
            throw new ProductException(ErrorCode.PRODUCT_ALREADY_DELETED);
        }

        LocalDateTime now = LocalDateTime.now();
        this.deletedAt = now;
        this.isActive = false;  // 삭제 시 비활성화도 함께
        this.updatedAt = now;
    }

    // ===== 상태 확인 메서드 =====

    /**
     * 주문 가능 여부
     */
    public boolean canOrder(int quantity) {
        if (!this.isActive) {
            return false;  // 비활성 상품
        }

        if (this.isOutOfStock) {
            return false;  // 품절
        }

        if (this.stock < quantity) {
            return false;  // 재고 부족
        }

        if (this.minOrderQuantity != null && quantity < this.minOrderQuantity) {
            return false;  // 최소 주문량 미달
        }

        if (this.maxOrderQuantity != null && quantity > this.maxOrderQuantity) {
            return false;  // 최대 주문량 초과
        }

        return true;
    }

    /**
     * 재고 있음 여부
     */
    public boolean hasStock() {
        return this.stock > 0;
    }

    /**
     * 활성 상품 여부
     */
    public boolean isActiveProduct() {
        return this.isActive;
    }

    /**
     * 품절 여부
     */
    public boolean isSoldOutProduct() {
        return this.isOutOfStock;
    }

    // ===== Validation 메서드 =====

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ProductException(ErrorCode.PRODUCT_NAME_REQUIRED);
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new ProductException(ErrorCode.PRODUCT_PRICE_REQUIRED);
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException(ErrorCode.PRODUCT_PRICE_INVALID);
        }
    }

    private static void validateStock(int stock) {
        if (stock < 0) {
            throw new ProductException(ErrorCode.PRODUCT_STOCK_INVALID);
        }
    }

    // ===== 테스트를 위한 ID 설정 메서드 (인메모리 DB용) =====
    public void setId(Long id) {
        this.id = id;
    }
}
