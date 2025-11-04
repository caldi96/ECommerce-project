package io.hhplus.ECommerce.ECommerce_project.product.usecase.command;

import java.math.BigDecimal;

public record CreateProductCommand(
        String name,
        String description,
        BigDecimal price,
        int stock,
        Long categoryId,
        Integer minOrderQuantity,
        Integer maxOrderQuantity
) {}
