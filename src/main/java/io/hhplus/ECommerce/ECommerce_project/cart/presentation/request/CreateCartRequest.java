package io.hhplus.ECommerce.ECommerce_project.cart.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.cart.application.command.CreateCartCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCartRequest(
        @NotNull(message = "userId는 필수입니다")
        @Min(value = 1, message = "userId는 1이상이어야 합니다")
        Long userId,

        @NotNull(message = "productId는 필수입니다")
        @Min(value = 1, message = "productId는 1이상이어야 합니다")
        Long productId,

        @NotNull(message = "상품 개수는 필수입니다")
        @Min(value = 1, message = "최소 상품 개수는 1 이상이어야 합니다")
        int quantity
) {
    public CreateCartCommand toCommand() {
        return new CreateCartCommand(
                userId,
                productId,
                quantity
        );
    }
}
