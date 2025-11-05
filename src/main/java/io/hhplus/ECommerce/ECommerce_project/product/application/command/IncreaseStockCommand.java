package io.hhplus.ECommerce.ECommerce_project.product.application.command;

public record IncreaseStockCommand(
        Long productId,
        int quantity
) {}