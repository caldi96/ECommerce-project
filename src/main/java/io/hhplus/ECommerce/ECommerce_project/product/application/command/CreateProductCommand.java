package io.hhplus.ECommerce.ECommerce_project.product.application.command;

import java.math.BigDecimal;

public record CreateProductCommand(
        String name,
        Long categoryId,
        String description,
        BigDecimal price,
        int stock,
        Integer minOrderQuantity,
        Integer maxOrderQuantity
) {}
