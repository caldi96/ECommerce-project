package io.hhplus.ECommerce.ECommerce_project.coupon.domain.enums;

public enum UserCouponStatus {

    AVAILABLE("사용 가능"),
    USED("사용됨"),
    EXPIRED("만료됨");

    private final String description;

    UserCouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
