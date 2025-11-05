package io.hhplus.ECommerce.ECommerce_project.product.application.command;

import java.math.BigDecimal;

public record UpdatePriceCommand(
        Long productId,
        BigDecimal price
) {}