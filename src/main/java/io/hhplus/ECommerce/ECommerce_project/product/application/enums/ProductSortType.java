package io.hhplus.ECommerce.ECommerce_project.product.application.enums;

public enum ProductSortType {

    LATEST("최신순"),
    POPULAR("판매량 높은순"),
    VIEWED("조회수 높은순"),
    PRICE_LOW("가격 낮은순"),
    PRICE_HIGH("가격 높은순");

    private final String description;

    ProductSortType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
