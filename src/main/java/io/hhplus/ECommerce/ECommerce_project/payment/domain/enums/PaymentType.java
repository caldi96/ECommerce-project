package io.hhplus.ECommerce.ECommerce_project.payment.domain.enums;

public enum PaymentType {

    PAYMENT("결제"),
    REFUND("환불");

    private final String description;

    PaymentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
