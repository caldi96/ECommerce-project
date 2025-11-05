package io.hhplus.ECommerce.ECommerce_project.product.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.product.application.command.DecreaseStockCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DecreaseStockRequest(
        @NotNull(message = "수량은 필수입니다")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다")
        Integer quantity
) {
    public DecreaseStockCommand toCommand(Long productId) {
        return new DecreaseStockCommand(
                productId,
                quantity
        );
    }
}