package io.hhplus.ECommerce.ECommerce_project.cart.application;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.repository.CartRepository;
import io.hhplus.ECommerce.ECommerce_project.common.exception.CartException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCartUseCase {

    private final CartRepository cartRepository;

    @Transactional
    public void execute(Long cartId, Long userId) {
        // 1. 장바구니 존재 여부 확인
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND));

        // 2. 사용자 검증 (다른 사용자의 장바구니 삭제 방지)
        if (!cart.isSameUser(userId)) {
            throw new CartException(ErrorCode.CART_ACCESS_DENIED);
        }

        // 3. 장바구니 상품 삭제
        cartRepository.deleteById(cartId);
    }
}
