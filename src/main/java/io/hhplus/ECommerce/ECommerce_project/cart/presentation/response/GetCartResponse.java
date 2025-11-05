package io.hhplus.ECommerce.ECommerce_project.cart.presentation.response;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetCartResponse(
        Long id,
        Long userId,
        Long productId,
        String productName,         // 상품명
        BigDecimal productPrice,    // 상품 가격
        int quantity,
        BigDecimal totalPrice,      // 가격 * 수량
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static GetCartResponse from(Cart cart, Product product) {
        return new GetCartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getProductId(),
                product.getName(),
                product.getPrice(),
                cart.getQuantity(),
                product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}
