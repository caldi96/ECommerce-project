package io.hhplus.ECommerce.ECommerce_project.cart.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.cart.application.command.UpdateQuantityCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateQuantityRequest(
        @NotNull(message = "사용자 ID는 필수입니다")
        Long userId,

        @NotNull(message = "수량은 필수입니다")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다")
        Integer quantity
) {
    public UpdateQuantityCommand toCommand(Long cartId) {
        return new UpdateQuantityCommand(
                cartId,
                userId,
                quantity
        );
    }
}