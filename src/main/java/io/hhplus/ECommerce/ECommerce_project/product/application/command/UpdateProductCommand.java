package io.hhplus.ECommerce.ECommerce_project.product.application.command;

import java.math.BigDecimal;

public record UpdateProductCommand(
        Long id,
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        boolean isActive,
        Integer minOrderQuantity,
        Integer maxOrderQuantity
) {}
