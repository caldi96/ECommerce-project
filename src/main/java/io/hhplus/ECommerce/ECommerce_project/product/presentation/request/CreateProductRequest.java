package io.hhplus.ECommerce.ECommerce_project.product.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.product.application.command.CreateProductCommand;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "상품명은 필수입니다")
        String name,

        Long categoryId,

        String description,

        @NotNull(message = "가격은 필수입니다")
        @DecimalMin(value = "0.0", inclusive = true, message = "가격은 0 이상이어야 합니다")
        BigDecimal price,

        @Min(value = 0, message = "재고는 0 이상이어야 합니다")
        int stock,

        @Min(value = 1, message = "최소 주문량은 1 이상이어야 합니다")
        Integer minOrderQuantity,

        @Min(value = 1, message = "최대 주문량은 1 이상이어야 합니다")
        Integer maxOrderQuantity
) {
    public CreateProductCommand toCommand() {
        return new CreateProductCommand(
                name,
                categoryId,
                description,
                price,
                stock,
                minOrderQuantity,
                maxOrderQuantity
        );
    }
}
