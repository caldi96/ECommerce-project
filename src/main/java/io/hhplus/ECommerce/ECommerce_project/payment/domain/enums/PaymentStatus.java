package io.hhplus.ECommerce.ECommerce_project.payment.domain.enums;

public enum PaymentStatus {

    PENDING("결제대기"),
    COMPLETED("결제완료"),
    FAILED("결제실패"),
    REFUNDED("환불완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
