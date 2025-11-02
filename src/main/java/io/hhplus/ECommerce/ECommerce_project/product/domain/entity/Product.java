package io.hhplus.ECommerce.ECommerce_project.product.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "category_id")
    // private Category category;
    private Long categoryId;
    private boolean isActive;
    private int soldCount;
    private Integer minOrderQuantity;
    private Integer maxOrderQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void decreaseStock(int quantity) {
        if (stock < quantity) throw new IllegalArgumentException("재고 부족");
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseSoldCount(int quantity) {
        this.soldCount += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseSoldCount(int quantity) {
        if (this.soldCount < quantity) throw new IllegalArgumentException("판매량이 취소량보다 작음");
        this.soldCount -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("재고는 음수가 될 수 없습니다.");
        }
        this.stock = quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMinOrderQuantity(int minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMaxOrderQuantity(int maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeActive() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeInactive() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
}
