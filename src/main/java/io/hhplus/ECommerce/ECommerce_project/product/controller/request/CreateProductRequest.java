package io.hhplus.ECommerce.ECommerce_project.product.controller.request;

import io.hhplus.ECommerce.ECommerce_project.product.usecase.command.CreateProductCommand;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        String description,
        BigDecimal price,
        int stock,
        Long categoryId,
        Integer minOrderQuantity,
        Integer maxOrderQuantity
) {
    public CreateProductCommand toCommand() {
        return new CreateProductCommand(
                name,
                description,
                price,
                stock,
                categoryId,
                minOrderQuantity,
                maxOrderQuantity
        );
    }
}
