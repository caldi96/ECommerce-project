package io.hhplus.ECommerce.ECommerce_project.order.presentation;

import io.hhplus.ECommerce.ECommerce_project.order.application.CreateOrderFromCartUseCase;
import io.hhplus.ECommerce.ECommerce_project.order.presentation.request.CreateOrderFromCartRequest;
import io.hhplus.ECommerce.ECommerce_project.order.presentation.response.CreateOrderFromCartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderFromCartUseCase createOrderFromCartUseCase;

    /**
     * 장바구니에서 주문 생성
     */
    @PostMapping("/from-cart")
    public ResponseEntity<CreateOrderFromCartResponse> createOrderFromCart(
            @Valid @RequestBody CreateOrderFromCartRequest request
    ) {
        CreateOrderFromCartResponse response = createOrderFromCartUseCase.execute(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
