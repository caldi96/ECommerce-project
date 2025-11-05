package io.hhplus.ECommerce.ECommerce_project.order.domain.constants;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.OrderException;

import java.math.BigDecimal;

/**
 * 배송비 정책 상수 클래스
 */
public final class ShippingPolicy {

    private ShippingPolicy() {
        // 인스턴스화 방지
        throw new AssertionError("Cannot instantiate ShippingPolicy");
    }

    /**
     * 무료 배송 기준 금액 (30,000원)
     */
    public static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("30000");

    /**
     * 기본 배송비 (3,000원)
     */
    public static final BigDecimal BASE_SHIPPING_FEE = new BigDecimal("3000");

    /**
     * 배송비 계산
     * - 30,000원 이상: 무료
     * - 30,000원 미만: 3,000원
     *
     * @param totalAmount 상품 총 금액
     * @return 배송비
     * @throws OrderException 주문 금액이 null인 경우
     */
    public static BigDecimal calculateShippingFee(BigDecimal totalAmount) {
        if (totalAmount == null) {
            throw new OrderException(ErrorCode.ORDER_TOTAL_AMOUNT_REQUIRED);
        }

        return totalAmount.compareTo(FREE_SHIPPING_THRESHOLD) >= 0
                ? BigDecimal.ZERO
                : BASE_SHIPPING_FEE;
    }
}