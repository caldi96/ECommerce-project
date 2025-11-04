package io.hhplus.ECommerce.ECommerce_project.product.controller.response;

import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        int stock,
        boolean isActive,
        boolean isSoldOut,
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
                product.isSoldOut(),
                product.getMinOrderQuantity(),
                product.getMaxOrderQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
