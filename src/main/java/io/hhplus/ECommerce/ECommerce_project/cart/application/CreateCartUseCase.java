package io.hhplus.ECommerce.ECommerce_project.cart.application;

import io.hhplus.ECommerce.ECommerce_project.cart.application.command.CreateCartCommand;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.repository.CartRepository;
import io.hhplus.ECommerce.ECommerce_project.common.exception.CartException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ProductException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.UserException;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import io.hhplus.ECommerce.ECommerce_project.user.domain.entity.User;
import io.hhplus.ECommerce.ECommerce_project.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateCartUseCase {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart execute(CreateCartCommand command) {
        // 1. 유저 존재 여부 확인
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        // 2. 상품 존재 여부 확인
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        // 3. 상품 주문 가능 여부 확인
        if (!product.canOrder(command.quantity())) {
            throw new CartException(ErrorCode.CART_PRODUCT_CANNOT_BE_ADDED_TO_CART);
        }

        // 4. 이미 해당 사용자의 장바구니에 같은 상품 존재 여부 확인
        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(command.userId(), command.productId());

        // 5-1. 이미 같은 상품이 존재하면 수량만 증가
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            // 증가될 총 수량 계산
            int newTotalQuantity = cart.getQuantity() + command.quantity();

            // 증가된 총 수량으로 주문 가능 여부 확인
            if (!product.canOrder(newTotalQuantity)) {
                throw new CartException(ErrorCode.CART_PRODUCT_CANNOT_BE_ADDED_TO_CART);
            }

            // 기존 수량에 추가
            cart.increaseQuantity(command.quantity());
            // 저장 후 반환
            return cartRepository.save(cart);
        }

        // 5-2. 도메인 생성
        Cart cart = Cart.createCart(
                command.userId(),
                command.productId(),
                command.quantity()
        );
        // 저장 후 반환
        return cartRepository.save(cart);
    }
}
