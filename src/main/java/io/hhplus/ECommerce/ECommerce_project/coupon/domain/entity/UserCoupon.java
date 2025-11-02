package io.hhplus.ECommerce.ECommerce_project.coupon.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.coupon.domain.enums.UserCouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {

    private Long id;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "coupon_id")
    // private Coupon coupon;
    private Long couponId;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id")
    // private User user;
    private Long userId;
    private UserCouponStatus status;    // ACTIVE, USED, EXPIRED
    private int usedCount;              // 현재 유저가 사용한 횟수
    private LocalDateTime usedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime issuedAt;

    // 쿠폰 사용 가능 여부
    public boolean canUse(int perUserLimit) {
        return this.status == UserCouponStatus.AVAILABLE && this.usedCount < perUserLimit;
    }

    // 쿠폰 사용시 사용량 증가
    public void increaseUsedCount(int perUserLimit) {
        this.usedCount++;
        if (this.usedCount >= perUserLimit) {
            this.status = UserCouponStatus.USED;
        }
        this.usedAt = LocalDateTime.now();
    }
}
