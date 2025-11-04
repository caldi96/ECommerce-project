package io.hhplus.ECommerce.ECommerce_project.order.domain.enums;

public enum OrderItemStatus {
    // 주문 대기 (초기 상태)
    ORDER_PENDING("주문대기"),

    // 주문 완료
    ORDER_COMPLETED("주문완료"),

    // 주문 취소
    ORDER_CANCELED("주문취소"),

    // 반품
    ORDER_RETURNED("반품완료"),

    // 환불
    ORDER_REFUNDED("환불완료"),

    // 구매 확정
    PURCHASE_CONFIRMED("구매확정");

    private final String description;

    OrderItemStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
