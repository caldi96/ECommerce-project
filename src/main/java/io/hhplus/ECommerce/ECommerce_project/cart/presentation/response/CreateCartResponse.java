package io.hhplus.ECommerce.ECommerce_project.cart.presentation.response;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCartResponse(
        Long id,
        Long userId,
        Long productId,
        int quantity,
        LocalDateTime createdAt
) {
    public static CreateCartResponse from(Cart cart) {
        return new CreateCartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getProductId(),
                cart.getQuantity(),
                cart.getCreatedAt()
        );
    }
}
