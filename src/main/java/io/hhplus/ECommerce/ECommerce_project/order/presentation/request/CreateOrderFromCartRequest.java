package io.hhplus.ECommerce.ECommerce_project.order.presentation.request;

import io.hhplus.ECommerce.ECommerce_project.order.application.command.CreateOrderFromCartCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderFromCartRequest(
        @NotNull(message = "사용자 ID는 필수입니다")
        Long userId,

        @NotEmpty(message = "장바구니 아이템은 최소 1개 이상이어야 합니다")
        List<Long> cartItemIds,

        BigDecimal pointAmount,  // 사용할 포인트 (선택, null 가능)

        Long couponId           // 사용할 쿠폰 ID (선택, null 가능)
) {
    public CreateOrderFromCartCommand toCommand() {
        return new CreateOrderFromCartCommand(
                userId,
                cartItemIds,
                pointAmount,
                couponId
        );
    }
}