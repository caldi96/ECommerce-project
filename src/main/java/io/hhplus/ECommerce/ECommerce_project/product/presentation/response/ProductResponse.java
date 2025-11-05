package io.hhplus.ECommerce.ECommerce_project.product.presentation.response;

import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
        Long id,
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        int stock,
        boolean isActive,
        boolean isOutOfStock,
        Integer minOrderQuantity,
        Integer maxOrderQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCategoryId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.isActive(),
                product.isOutOfStock(),
                product.getMinOrderQuantity(),
                product.getMaxOrderQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public static List<ProductResponse> from(List<Product> productList) {
        return productList.stream()
                .map(product -> ProductResponse.from(product))
                .toList();
    }
}
