package io.hhplus.ECommerce.ECommerce_project.product.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.product.application.command.UpdatePriceCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdatePriceRequest(
        @NotNull(message = "가격은 필수입니다")
        @DecimalMin(value = "0.0", inclusive = true, message = "가격은 0 이상이어야 합니다")
        BigDecimal price
) {
    public UpdatePriceCommand toCommand(Long productId) {
        return new UpdatePriceCommand(
                productId,
                price
        );
    }
}
