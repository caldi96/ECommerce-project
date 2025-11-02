package io.hhplus.ECommerce.ECommerce_project.coupon.domain.entity;

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

    // 쿠폰을 활성화 상태로 변경
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    // 쿠폰을 비활성화 상태로 변경
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    // 현재 쿠폰이 유효한지 검사
    public boolean isAvailableNow(LocalDateTime now) {
        return this.isActive
                && (this.startDate == null || !this.startDate.isAfter(now))
                && (this.endDate == null || !this.endDate.isBefore(now));
    }

    // 쿠폰 발급시 수량 증가
    public void increaseIssuedQuantity() {
        if (this.issuedQuantity >= this.totalQuantity) {
            throw new IllegalStateException("모든 쿠폰이 이미 발급되었습니다.");
        }
        this.issuedQuantity++;
        this.updatedAt = LocalDateTime.now();
    }

    // 쿠폰 사용시 사용량 증가
    public void increaseUsageCount() {
        if (this.usageCount >= this.totalQuantity) {
            throw new IllegalStateException("모든 쿠폰이 이미 사용되었습니다.");
        }
        this.usageCount++;
        this.updatedAt = LocalDateTime.now();
    }

    // 쿠폰 사용 가능 여부 (수량 기준)
    public boolean hasRemainingQuantity() {
        return this.issuedQuantity < this.totalQuantity;
    }
}
