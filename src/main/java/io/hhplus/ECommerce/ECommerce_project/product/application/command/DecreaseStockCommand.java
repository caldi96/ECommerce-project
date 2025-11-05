package io.hhplus.ECommerce.ECommerce_project.product.application.command;

public record DecreaseStockCommand(
        Long productId,
        int quantity
) {}