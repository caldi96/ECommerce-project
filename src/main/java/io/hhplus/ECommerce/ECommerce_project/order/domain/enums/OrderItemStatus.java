package io.hhplus.ECommerce.ECommerce_project.order.domain.enums;

public enum OrderItemStatus {

    // 주문 취소
    CANCEL_REQUESTED("주문취소 요청"),
    CANCEL_APPROVED("주문취소 승인"),
    CANCEL_REJECTED("주문취소 거절"),

    // 반품
    RETURN_REQUESTED("반품 요청"),
    RETURN_IN_PROGRESS("반품 배송중"),
    RETURN_REJECTED("반품 거절"),
    RETURN_APPROVED("반품 승인"),

    // 환불
    REFUND_REQUESTED("환불 요청"),
    REFUND_IN_PROGRESS("환불 진행중"),
    REFUND_COMPLETED("환불 완료"),
    REFUND_REJECTED("환불 거절"),

    // 교환
    EXCHANGE_REQUESTED("교환 요청"),
    EXCHANGE_APPROVED("교환 승인"),
    EXCHANGE_REJECTED("교환 거절"),

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
