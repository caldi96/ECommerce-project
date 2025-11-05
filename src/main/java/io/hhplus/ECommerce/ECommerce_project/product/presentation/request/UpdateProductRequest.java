package io.hhplus.ECommerce.ECommerce_project.product.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.product.application.command.UpdateProductCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateProductRequest(
        Long categoryId,

        @NotBlank(message = "상품명은 필수입니다")
        String name,

        String description,

        @NotNull(message = "가격은 필수입니다")
        @DecimalMin(value = "0.0", inclusive = true, message = "가격은 0 이상이어야 합니다")
        BigDecimal price,

        @NotNull(message = "활성화 상태는 필수입니다")
        Boolean isActive,

        @Min(value = 1, message = "최소 주문량은 1 이상이어야 합니다")
        Integer minOrderQuantity,

        @Min(value = 1, message = "최대 주문량은 1 이상이어야 합니다")
        Integer maxOrderQuantity
) {
    public UpdateProductCommand toCommand(Long id) {
        return new UpdateProductCommand(
                id,
                categoryId,
                name,
                description,
                price,
                isActive,
                minOrderQuantity,
                maxOrderQuantity
        );
    }
}
