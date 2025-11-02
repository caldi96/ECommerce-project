package io.hhplus.ECommerce.ECommerce_project.payment.domain.enums;

public enum PaymentMethod {

    CARD("신용카드"),
    BANK_TRANSFER("계좌이체"),
    KAKAO_PAY("카카오페이"),
    TOSS("토스");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
