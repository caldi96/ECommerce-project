package io.hhplus.ECommerce.ECommerce_project.coupon.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.CouponException;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
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

    /**
     * 쿠폰 사용 가능 여부 확인
     */
    public boolean canUse(int perUserLimit) {
        return this.status == UserCouponStatus.AVAILABLE && this.usedCount < perUserLimit;
    }

    /**
     * 쿠폰 사용 가능 여부 검증 (예외 발생)
     */
    public void validateCanUse(int perUserLimit) {
        if (this.status == UserCouponStatus.USED) {
            throw new CouponException(ErrorCode.USER_COUPON_ALREADY_USED);
        }

        if (this.status == UserCouponStatus.EXPIRED) {
            throw new CouponException(ErrorCode.COUPON_EXPIRED);
        }

        if (this.status != UserCouponStatus.AVAILABLE) {
            throw new CouponException(ErrorCode.USER_COUPON_NOT_AVAILABLE);
        }

        if (this.usedCount >= perUserLimit) {
            throw new CouponException(ErrorCode.COUPON_USAGE_LIMIT_EXCEEDED);
        }
    }

    /**
     * 쿠폰 사용 처리
     */
    public void use(int perUserLimit) {
        validateCanUse(perUserLimit);

        this.usedCount++;
        this.usedAt = LocalDateTime.now();

        // 사용 횟수 제한에 도달하면 USED 상태로 변경
        if (this.usedCount >= perUserLimit) {
            this.status = UserCouponStatus.USED;
        }
    }

    /**
     * 쿠폰 만료 처리
     */
    public void expire() {
        if (this.status == UserCouponStatus.USED) {
            throw new CouponException(ErrorCode.USER_COUPON_ALREADY_USED);
        }

        if (this.status == UserCouponStatus.EXPIRED) {
            throw new CouponException(ErrorCode.COUPON_EXPIRED);
        }

        this.status = UserCouponStatus.EXPIRED;
        this.expiredAt = LocalDateTime.now();
    }
}
