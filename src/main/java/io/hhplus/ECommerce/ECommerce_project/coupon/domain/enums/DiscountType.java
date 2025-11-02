package io.hhplus.ECommerce.ECommerce_project.coupon.domain.enums;

public enum DiscountType {

    PERCENTAGE("정률할인"),
    FIXED("정액할인");

    private final String description;

    DiscountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
