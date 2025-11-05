package io.hhplus.ECommerce.ECommerce_project.cart.presentation;

import io.hhplus.ECommerce.ECommerce_project.cart.application.CreateCartUseCase;
import io.hhplus.ECommerce.ECommerce_project.cart.application.DeleteCartUseCase;
import io.hhplus.ECommerce.ECommerce_project.cart.application.GetCartUseCase;
import io.hhplus.ECommerce.ECommerce_project.cart.application.UpdateQuantityUseCase;
import io.hhplus.ECommerce.ECommerce_project.cart.presentation.request.CreateCartRequest;
import io.hhplus.ECommerce.ECommerce_project.cart.presentation.request.UpdateQuantityRequest;
import io.hhplus.ECommerce.ECommerce_project.cart.presentation.response.CreateCartResponse;
import io.hhplus.ECommerce.ECommerce_project.cart.presentation.response.GetCartResponse;
import io.hhplus.ECommerce.ECommerce_project.cart.presentation.response.UpdateQuantityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CreateCartUseCase createCartUseCase;
    private final GetCartUseCase getCartUseCase;
    private final UpdateQuantityUseCase updateQuantityUseCase;
    private final DeleteCartUseCase deleteCartUseCase;

    /**
     * 상품 장바구니 등록
     */
    @PostMapping
    public ResponseEntity<CreateCartResponse> createCart(@Valid @RequestBody CreateCartRequest request) {
        var createdCart = createCartUseCase.execute(request.toCommand());
        return ResponseEntity.ok(CreateCartResponse.from(createdCart));
    }

    /**
     * 장바구니 상품 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<GetCartResponse>> getCartList(@PathVariable Long userId) {
        var cartList = getCartUseCase.execute(userId);  // List<Cart> 반환

        return ResponseEntity.ok(cartList);
    }

    /**
     * 장바구니 상품 수량 수정
     */
    @PatchMapping("/{cartId}/quantity")
    public ResponseEntity<UpdateQuantityResponse> updateQuantity(
            @PathVariable Long cartId,
            @Valid @RequestBody UpdateQuantityRequest request
    ) {
        var response = updateQuantityUseCase.execute(request.toCommand(cartId));
        return ResponseEntity.ok(response);
    }

    /**
     * 장바구니 상품 삭제
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(
            @PathVariable Long cartId,
            @RequestParam Long userId
    ) {
        deleteCartUseCase.execute(cartId, userId);
        return ResponseEntity.noContent().build();
    }
}
