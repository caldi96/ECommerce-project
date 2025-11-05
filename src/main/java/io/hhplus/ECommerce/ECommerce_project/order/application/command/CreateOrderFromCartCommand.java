package io.hhplus.ECommerce.ECommerce_project.order.application.command;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderFromCartCommand(
        Long userId,
        List<Long> cartItemIds,
        BigDecimal pointAmount,  // 사용할 포인트 (선택, null 가능)
        Long couponId           // 사용할 쿠폰 ID (선택, null 가능)
) {}
