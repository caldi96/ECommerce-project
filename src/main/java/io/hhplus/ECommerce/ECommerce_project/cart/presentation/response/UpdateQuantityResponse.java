package io.hhplus.ECommerce.ECommerce_project.cart.presentation.response;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateQuantityResponse(
        Long cartId,
        Long productId,
        String productName,
        BigDecimal productPrice,    // 단가
        int quantity,               // 변경된 수량
        BigDecimal totalPrice,      // 단가 * 수량 (계산)
        LocalDateTime updatedAt
) {
    public static UpdateQuantityResponse from(Cart cart, Product product) {
        return new UpdateQuantityResponse(
                cart.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(), // 최신 단가
                cart.getQuantity(), // 변경된 수량
                product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),    // 총 가격 계산
                cart.getUpdatedAt()
        );
    }
}
