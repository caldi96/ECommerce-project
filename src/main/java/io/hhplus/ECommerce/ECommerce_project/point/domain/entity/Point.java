package io.hhplus.ECommerce.ECommerce_project.point.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.PointException;
import io.hhplus.ECommerce.ECommerce_project.point.domain.enums.PointType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 정적 팩토리 메서드를 위한 private 생성자
public class Point {

    private Long id;

    // 나중에 JPA 연결 시
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id")
    //private User user;
    private Long userId;
    private BigDecimal amount;
    private PointType pointType;
    private String description;

    // 나중에 JPA 연결 시
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "order_id")
    //private Order order;
    private Long orderId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean isExpired;  // 만료 여부
    private boolean isUsed;     // 사용 여부

    // ===== 정적 팩토리 메서드 =====

    /**
     * 포인트 충전
     */
    public static Point charge(Long userId, BigDecimal amount, String description) {
        validateUserId(userId);
        validateAmount(amount);
        validateDescription(description);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusYears(1);  // 1년 후 만료

        return new Point(
            null,  // id는 저장 시 생성
            userId,
            amount,
            PointType.CHARGE,
            description,
            null,  // orderId (충전은 주문과 관계 없음)
            now,   // createdAt
            expiresAt,
            false, // isExpired
            false  // isUsed
        );
    }

    /**
     * 포인트 사용 (주문 결제)
     */
    public static Point use(Long userId, Long orderId, BigDecimal amount, String description) {
        validateUserId(userId);
        validateOrderId(orderId);
        validateAmount(amount);
        validateDescription(description);

        LocalDateTime now = LocalDateTime.now();

        return new Point(
            null,
            userId,
            amount,
            PointType.USE,
            description,
            orderId,
            now,
            null,  // 사용 포인트는 만료일 없음
            false,
            true   // 사용 시점에 이미 사용됨
        );
    }

    /**
     * 포인트 환불
     */
    public static Point refund(Long userId, Long orderId, BigDecimal amount, String description) {
        validateUserId(userId);
        validateOrderId(orderId);
        validateAmount(amount);
        validateDescription(description);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusYears(1);

        return new Point(
            null,
            userId,
            amount,
            PointType.REFUND,
            description,
            orderId,
            now,
            expiresAt,
            false,
            false
        );
    }

    // ===== 비즈니스 로직 메서드 =====

    /**
     * 포인트 만료 처리
     */
    public void expire() {
        if (this.pointType == PointType.USE || this.isUsed) {
            throw new PointException(ErrorCode.POINT_CANNOT_EXPIRE_USED_POINT);
        }

        if (this.isExpired) {
            throw new PointException(ErrorCode.POINT_ALREADY_EXPIRED);
        }

        if (this.expiresAt == null) {
            throw new PointException(ErrorCode.POINT_NO_EXPIRATION_DATE);
        }

        this.isExpired = true;
    }

    /**
     * 포인트 사용 처리 (실제 사용 시점 표시)
     */
    public void markAsUsed() {
        if (this.pointType != PointType.CHARGE && this.pointType != PointType.REFUND) {
            throw new PointException(ErrorCode.POINT_ONLY_CHARGE_OR_REFUND_CAN_BE_USED);
        }

        if (this.isUsed) {
            throw new PointException(ErrorCode.POINT_ALREADY_USED);
        }

        if (this.isExpired) {
            throw new PointException(ErrorCode.POINT_EXPIRED_CANNOT_USE);
        }

        if (this.expiresAt != null && LocalDateTime.now().isAfter(this.expiresAt)) {
            throw new PointException(ErrorCode.POINT_EXPIRATION_DATE_PASSED);
        }

        this.isUsed = true;
    }

    // ===== 상태 확인 메서드 =====

    /**
     * 사용 가능한 포인트인지 확인
     */
    public boolean isAvailable() {
        if (this.pointType == PointType.USE) {
            return false;  // 사용 타입은 사용 불가
        }

        if (this.isUsed || this.isExpired) {
            return false;
        }

        if (this.expiresAt != null && LocalDateTime.now().isAfter(this.expiresAt)) {
            return false;
        }

        return true;
    }

    /**
     * 포인트가 만료되었는지 확인
     */
    public boolean checkExpired() {
        if (this.expiresAt == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    /**
     * 충전 포인트인지 확인
     */
    public boolean isChargeType() {
        return this.pointType == PointType.CHARGE;
    }

    /**
     * 사용 포인트인지 확인
     */
    public boolean isUseType() {
        return this.pointType == PointType.USE;
    }

    /**
     * 환불 포인트인지 확인
     */
    public boolean isRefundType() {
        return this.pointType == PointType.REFUND;
    }

    // ===== Validation 메서드 =====

    private static void validateUserId(Long userId) {
        if (userId == null) {
            throw new PointException(ErrorCode.USER_ID_REQUIRED);
        }
    }

    private static void validateOrderId(Long orderId) {
        if (orderId == null) {
            throw new PointException(ErrorCode.POINT_ORDER_ID_REQUIRED);
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new PointException(ErrorCode.POINT_AMOUNT_REQUIRED);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PointException(ErrorCode.POINT_AMOUNT_INVALID);
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new PointException(ErrorCode.POINT_DESCRIPTION_REQUIRED);
        }
    }

    // ===== 테스트를 위한 ID 설정 메서드 (인메모리 DB용) =====
    public void setId(Long id) {
        this.id = id;
    }
}
