package io.hhplus.ECommerce.ECommerce_project.order.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.OrderException;
import io.hhplus.ECommerce.ECommerce_project.order.domain.enums.OrderItemStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 정적 팩토리 메서드를 위한 private 생성자
public class OrderItem {

    private Long id;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_id")
    // private Product product;
    private Long productId;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "order_id")
    // private Order order;
    private Long orderId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    private OrderItemStatus status;
    private LocalDateTime confirmedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime returnedAt;
    private LocalDateTime refundedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== 정적 팩토리 메서드 =====

    /**
     * 주문 항목 생성
     */
    public static OrderItem createOrderItem(
        Long orderId,
        Long productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
    ) {
        validateOrderId(orderId);
        validateProductId(productId);
        validateProductName(productName);
        validateQuantity(quantity);
        validateUnitPrice(unitPrice);

        // subTotal 계산
        BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

        LocalDateTime now = LocalDateTime.now();
        return new OrderItem(
            null,  // id는 저장 시 생성
            productId,
            orderId,
            productName,
            quantity,
            unitPrice,
            subTotal,
            OrderItemStatus.ORDER_PENDING,  // 초기 상태
            null,  // confirmedAt
            null,  // canceledAt
            null,  // returnedAt
            null,  // refundedAt
            now,   // createdAt
            now    // updatedAt
        );
    }

    // ===== 비즈니스 로직 메서드 =====

    /**
     * 주문 항목 완료 처리
     */
    public void complete() {
        if (this.status != OrderItemStatus.ORDER_PENDING) {
            throw new OrderException(ErrorCode.ORDER_ITEM_INVALID_STATUS_FOR_COMPLETE,
                "대기 중인 주문 항목만 완료 처리할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderItemStatus.ORDER_COMPLETED;
        this.updatedAt = now;
    }

    /**
     * 주문 항목 취소 (주문 완료 전)
     */
    public void cancel() {
        if (this.status != OrderItemStatus.ORDER_PENDING) {
            throw new OrderException(ErrorCode.ORDER_ITEM_INVALID_STATUS_FOR_CANCEL,
                "대기 중인 주문 항목만 취소할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderItemStatus.ORDER_CANCELED;
        this.canceledAt = now;
        this.updatedAt = now;
    }

    /**
     * 주문 항목 취소 (주문 완료 후)
     */
    public void cancelAfterComplete() {
        if (this.status != OrderItemStatus.ORDER_COMPLETED) {
            throw new OrderException(ErrorCode.ORDER_ITEM_INVALID_STATUS_FOR_CANCEL,
                "완료된 주문 항목만 취소할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderItemStatus.ORDER_CANCELED;
        this.canceledAt = now;
        this.updatedAt = now;
    }

    /**
     * 상품 항목 반품
     */
    public void returnItem() {
        if (this.status != OrderItemStatus.ORDER_COMPLETED && this.status != OrderItemStatus.PURCHASE_CONFIRMED) {
            throw new OrderException(ErrorCode.ORDER_ITEM_INVALID_STATUS_FOR_RETURN,
                "완료 또는 구매 확정된 주문 항목만 반품할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderItemStatus.ORDER_RETURNED;
        this.returnedAt = now;
        this.updatedAt = now;
    }

    /**
     * 상품 항목 환불
     */
    public void refund() {
        if (this.status != OrderItemStatus.ORDER_CANCELED && this.status != OrderItemStatus.ORDER_RETURNED) {
            throw new OrderException(ErrorCode.ORDER_ITEM_INVALID_STATUS_FOR_REFUND,
                "취소 또는 반품된 주문 항목만 환불할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderItemStatus.ORDER_REFUNDED;
        this.refundedAt = now;
        this.updatedAt = now;
    }

    /**
     * 상품 항목 구매 확정
     */
    public void confirmPurchase() {
        if (this.status != OrderItemStatus.ORDER_COMPLETED) {
            throw new OrderException(ErrorCode.ORDER_ITEM_INVALID_STATUS_FOR_CONFIRM,
                "완료된 주문 항목만 구매 확정할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderItemStatus.PURCHASE_CONFIRMED;
        this.confirmedAt = now;
        this.updatedAt = now;
    }

    /**
     * 주문 항목 총 금액 재계산
     */
    public void recalculateSubTotal() {
        validateUnitPrice(this.unitPrice);
        validateQuantity(this.quantity);

        this.subTotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        this.updatedAt = LocalDateTime.now();
    }

    // ===== 상태 확인 메서드 =====

    /**
     * 대기 중 여부
     */
    public boolean isPending() {
        return this.status == OrderItemStatus.ORDER_PENDING;
    }

    /**
     * 완료 여부
     */
    public boolean isCompleted() {
        return this.status == OrderItemStatus.ORDER_COMPLETED;
    }

    /**
     * 취소 여부
     */
    public boolean isCanceled() {
        return this.status == OrderItemStatus.ORDER_CANCELED;
    }

    /**
     * 반품 여부
     */
    public boolean isReturned() {
        return this.status == OrderItemStatus.ORDER_RETURNED;
    }

    /**
     * 환불 여부
     */
    public boolean isRefunded() {
        return this.status == OrderItemStatus.ORDER_REFUNDED;
    }

    /**
     * 구매 확정 여부
     */
    public boolean isPurchaseConfirmed() {
        return this.status == OrderItemStatus.PURCHASE_CONFIRMED;
    }

    /**
     * 반품 가능 여부
     */
    public boolean canReturn() {
        return this.status == OrderItemStatus.ORDER_COMPLETED
            || this.status == OrderItemStatus.PURCHASE_CONFIRMED;
    }

    /**
     * 환불 가능 여부
     */
    public boolean canRefund() {
        return this.status == OrderItemStatus.ORDER_CANCELED
            || this.status == OrderItemStatus.ORDER_RETURNED;
    }

    // ===== Validation 메서드 =====

    private static void validateOrderId(Long orderId) {
        if (orderId == null) {
            throw new OrderException(ErrorCode.ORDER_ITEM_ORDER_ID_REQUIRED);
        }
    }

    private static void validateProductId(Long productId) {
        if (productId == null) {
            throw new OrderException(ErrorCode.ORDER_ITEM_PRODUCT_ID_REQUIRED);
        }
    }

    private static void validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new OrderException(ErrorCode.ORDER_ITEM_PRODUCT_NAME_REQUIRED);
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new OrderException(ErrorCode.ORDER_ITEM_QUANTITY_INVALID);
        }
    }

    private static void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null) {
            throw new OrderException(ErrorCode.ORDER_ITEM_UNIT_PRICE_REQUIRED);
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(ErrorCode.ORDER_ITEM_UNIT_PRICE_INVALID);
        }
    }

    // ===== 테스트를 위한 ID 설정 메서드 (인메모리 DB용) =====
    public void setId(Long id) {
        this.id = id;
    }
}
