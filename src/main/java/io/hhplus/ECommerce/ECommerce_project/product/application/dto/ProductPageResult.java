package io.hhplus.ECommerce.ECommerce_project.product.application.dto;

import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductPageResult {
    private final List<Product> products;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    public ProductPageResult(List<Product> products, int page, int size, long totalElements) {
        this.products = products;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.first = page == 0;
        this.last = page >= totalPages - 1;
    }
}