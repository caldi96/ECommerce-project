package io.hhplus.ECommerce.ECommerce_project.cart.application.command;

public record UpdateQuantityCommand(
        Long cartId,
        Long userId,
        int quantity
) {}
