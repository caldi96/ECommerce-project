package io.hhplus.ECommerce.ECommerce_project.cart.application.command;

public record CreateCartCommand(
        Long userId,
        Long productId,
        int quantity
) {}
