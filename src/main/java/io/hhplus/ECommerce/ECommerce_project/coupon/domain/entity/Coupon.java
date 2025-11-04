package io.hhplus.ECommerce.ECommerce_project.coupon.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.CouponException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.coupon.domain.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    private Long id;
    private String name;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderAmount;
    private int totalQuantity;              // 전체 수량
    private int issuedQuantity;             // 발급된 양
    private int usageCount;                 // 사용된 양
    private int perUserLimit;               // 인당 사용가능 양
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 쿠폰 활성화
     */
    public void activate() {
        if (this.isActive) {
            throw new CouponException(ErrorCode.COUPON_ALREADY_ACTIVE);
        }
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 쿠폰 비활성화
     */
    public void deactivate() {
        if (!this.isActive) {
            throw new CouponException(ErrorCode.COUPON_ALREADY_INACTIVE);
        }
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 현재 시점 기준 쿠폰 유효성 검사 (편의 메서드)
     */
    public boolean isAvailableNow() {
        return isAvailableNow(LocalDateTime.now());
    }

    /**
     * 특정 시점 기준 쿠폰 유효성 검사
     * @param now 검사 기준 시점
     * @return 쿠폰 사용 가능 여부
     */
    public boolean isAvailableNow(LocalDateTime now) {
        if (!this.isActive) {
            return false;
        }

        if (this.startDate != null && this.startDate.isAfter(now)) {
            return false;  // 아직 시작 안 됨
        }

        if (this.endDate != null && this.endDate.isBefore(now)) {
            return false;  // 이미 만료됨
        }

        return true;
    }

    /**
     * 쿠폰 발급 시 수량 증가
     */
    public void increaseIssuedQuantity() {
        if (this.issuedQuantity >= this.totalQuantity) {
            throw new CouponException(ErrorCode.COUPON_ALL_ISSUED);
        }
        this.issuedQuantity++;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 쿠폰 사용 시 사용량 증가
     */
    public void increaseUsageCount() {
        if (this.usageCount >= this.totalQuantity) {
            throw new CouponException(ErrorCode.COUPON_ALL_USED);
        }
        this.usageCount++;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 발급 가능 여부 (수량 기준)
     */
    public boolean hasRemainingQuantity() {
        return this.issuedQuantity < this.totalQuantity;
    }

    /**
     * 현재 시점 기준 쿠폰 유효성 검증 (편의 메서드)
     */
    public void validateAvailability() {
        validateAvailability(LocalDateTime.now());
    }

    /**
     * 특정 시점 기준 쿠폰 유효성 검증 (예외 발생)
     * @param now 검사 기준 시점
     * @throws CouponException 쿠폰 사용 불가 시
     */
    public void validateAvailability(LocalDateTime now) {
        if (!this.isActive) {
            throw new CouponException(ErrorCode.COUPON_NOT_AVAILABLE);
        }

        if (this.startDate != null && this.startDate.isAfter(now)) {
            throw new CouponException(ErrorCode.COUPON_NOT_STARTED);
        }

        if (this.endDate != null && this.endDate.isBefore(now)) {
            throw new CouponException(ErrorCode.COUPON_EXPIRED);
        }
    }

    /**
     * 주문 금액에 대한 할인 금액 계산
     * @param orderAmount 주문 금액
     * @return 할인 금액
     */
    public BigDecimal calculateDiscountAmount(BigDecimal orderAmount) {
        if (orderAmount == null || orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // 최소 주문 금액 검증
        if (this.minOrderAmount != null && orderAmount.compareTo(this.minOrderAmount) < 0) {
            throw new CouponException(ErrorCode.COUPON_MIN_ORDER_AMOUNT_NOT_MET,
                "최소 주문 금액을 충족하지 못했습니다. 최소 주문 금액: " + this.minOrderAmount + ", 현재 주문 금액: " + orderAmount);
        }

        BigDecimal discountAmount;

        if (this.discountType == DiscountType.PERCENTAGE) {
            // 정률 할인: 주문 금액 * (할인율 / 100)
            discountAmount = orderAmount.multiply(this.discountValue).divide(new BigDecimal("100"), 0, BigDecimal.ROUND_DOWN);

            // 최대 할인 금액 제한 적용
            if (this.maxDiscountAmount != null && discountAmount.compareTo(this.maxDiscountAmount) > 0) {
                discountAmount = this.maxDiscountAmount;
            }
        } else if (this.discountType == DiscountType.FIXED) {
            // 정액 할인: 고정 금액
            discountAmount = this.discountValue;

            // 주문 금액보다 할인 금액이 클 수 없음
            if (discountAmount.compareTo(orderAmount) > 0) {
                discountAmount = orderAmount;
            }
        } else {
            throw new CouponException(ErrorCode.COUPON_INVALID_DISCOUNT_TYPE,
                "지원하지 않는 할인 타입입니다: " + this.discountType);
        }

        return discountAmount;
    }
}
