package io.hhplus.ECommerce.ECommerce_project.cart.application;

import io.hhplus.ECommerce.ECommerce_project.cart.application.command.UpdateQuantityCommand;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.repository.CartRepository;
import io.hhplus.ECommerce.ECommerce_project.cart.presentation.response.UpdateQuantityResponse;
import io.hhplus.ECommerce.ECommerce_project.common.exception.CartException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ProductException;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateQuantityUseCase {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UpdateQuantityResponse execute(UpdateQuantityCommand command) {
        // 1. 장바구니 조회
        Cart cart = cartRepository.findById(command.cartId())
                .orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND));

        // 2. 사용자 검증 (다른 사용자의 장바구니 수정 방지)
        if (!cart.isSameUser(command.userId())) {
            throw new CartException(ErrorCode.CART_ACCESS_DENIED);
        }

        // 3. 상품 정보 조회
        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        // 4. 상품 주문 가능 여부 확인 (새로운 수량으로)
        if (!product.canOrder(command.quantity())) {
            throw new CartException(ErrorCode.CART_PRODUCT_CANNOT_BE_ADDED_TO_CART);
        }

        // 5. 수량 변경
        cart.changeQuantity(command.quantity());

        // 6. 변경사항 저장
        Cart updatedCart = cartRepository.save(cart);

        // 7. Response 생성 (총 가격 자동 계산됨)
        return UpdateQuantityResponse.from(updatedCart, product);
    }
}
