package io.hhplus.ECommerce.ECommerce_project.order.domain.enums;

public enum OrderStatus {

    PENDING("주문중"),
    PAID("결제완료"),
    COMPLETED("주문완료"),
    PAYMENT_FAILED("결제실패"),
    CANCELED("주문취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
