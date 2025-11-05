package io.hhplus.ECommerce.ECommerce_project.product.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.product.application.command.IncreaseStockCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record IncreaseStockRequest(
        @NotNull(message = "수량은 필수입니다")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다")
        Integer quantity
) {
    public IncreaseStockCommand toCommand(Long productId) {
        return new IncreaseStockCommand(
                productId,
                quantity
        );
    }
}