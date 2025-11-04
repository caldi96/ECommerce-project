package io.hhplus.ECommerce.ECommerce_project.order.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.OrderException;
import io.hhplus.ECommerce.ECommerce_project.order.domain.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 정적 팩토리 메서드를 위한 private 생성자
public class Orders {

    private Long id;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id")
    // private User user;
    private Long userId;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "coupon_id")
    // private Coupon coupon;
    private Long couponId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private BigDecimal shippingFee;
    private OrderStatus status;
    private BigDecimal pointAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
    private LocalDateTime canceledAt;

    // ===== 정적 팩토리 메서드 =====

    /**
     * 주문 생성
     */
    public static Orders createOrder(
        Long userId,
        BigDecimal totalAmount,
        BigDecimal shippingFee,
        Long couponId,
        BigDecimal discountAmount,
        BigDecimal pointAmount
    ) {
        validateUserId(userId);
        validateAmount(totalAmount, "총 상품 금액");
        validateAmount(shippingFee, "배송비");

        // 할인 금액 검증 (null 가능)
        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(ErrorCode.ORDER_DISCOUNT_AMOUNT_INVALID);
        }

        // 포인트 금액 검증 (null 가능)
        if (pointAmount != null && pointAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(ErrorCode.ORDER_POINT_AMOUNT_INVALID);
        }

        // 최종 금액 계산
        BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        BigDecimal point = pointAmount != null ? pointAmount : BigDecimal.ZERO;
        BigDecimal finalAmount = totalAmount.add(shippingFee).subtract(discount).subtract(point);

        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(ErrorCode.ORDER_FINAL_AMOUNT_NEGATIVE);
        }

        LocalDateTime now = LocalDateTime.now();

        return new Orders(
            null,  // id는 저장 시 생성
            userId,
            couponId,
            totalAmount,
            discount,
            finalAmount,
            shippingFee,
            OrderStatus.PENDING,  // 초기 상태는 PENDING
            point,
            now,   // createdAt
            now,   // updatedAt
            null,  // paidAt
            null   // canceledAt
        );
    }

    // ===== 비즈니스 로직 메서드 =====

    /**
     * 결제 완료 처리
     */
    public void paid() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderException(ErrorCode.ORDER_INVALID_STATUS_FOR_PAYMENT,
                "대기 중인 주문만 결제할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderStatus.PAID;
        this.paidAt = now;
        this.updatedAt = now;
    }

    /**
     * 주문 취소 처리 (결제 전)
     */
    public void cancel() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderException(ErrorCode.ORDER_INVALID_STATUS_FOR_CANCEL,
                "대기 중인 주문만 취소할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderStatus.CANCELED;
        this.canceledAt = now;
        this.updatedAt = now;
    }

    /**
     * 결제 취소 처리 -> 주문 취소 (결제 후)
     */
    public void cancelAfterPaid() {
        if (this.status != OrderStatus.PAID) {
            throw new OrderException(ErrorCode.ORDER_INVALID_STATUS_FOR_CANCEL,
                "결제 완료된 주문만 결제 취소할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderStatus.CANCELED;
        this.canceledAt = now;
        this.updatedAt = now;
    }

    /**
     * 주문 완료 처리
     */
    public void complete() {
        if (this.status != OrderStatus.PAID) {
            throw new OrderException(ErrorCode.ORDER_INVALID_STATUS_FOR_COMPLETE,
                "결제 완료된 주문만 완료 처리할 수 있습니다. 현재 상태: " + this.status);
        }

        LocalDateTime now = LocalDateTime.now();
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = now;
    }

    // ===== 상태 확인 메서드 =====

    /**
     * 결제 완료 여부
     */
    public boolean isPaid() {
        return this.status == OrderStatus.PAID;
    }

    /**
     * 주문 취소 여부
     */
    public boolean isCanceled() {
        return this.status == OrderStatus.CANCELED;
    }

    /**
     * 주문 완료 여부
     */
    public boolean isCompleted() {
        return this.status == OrderStatus.COMPLETED;
    }

    /**
     * 대기 중 여부
     */
    public boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    /**
     * 취소 가능 여부 (PENDING 상태만)
     */
    public boolean canCancel() {
        return this.status == OrderStatus.PENDING;
    }

    /**
     * 결제 취소 가능 여부 (PAID 상태만)
     */
    public boolean canCancelAfterPaid() {
        return this.status == OrderStatus.PAID;
    }

    /**
     * 무료 배송 여부
     */
    public boolean isFreeShipping() {
        return this.shippingFee.compareTo(BigDecimal.ZERO) == 0;
    }

    // ===== Validation 메서드 =====

    private static void validateUserId(Long userId) {
        if (userId == null) {
            throw new OrderException(ErrorCode.ORDER_USER_ID_REQUIRED);
        }
    }

    private static void validateAmount(BigDecimal amount, String fieldName) {
        if (amount == null) {
            throw new OrderException(ErrorCode.ORDER_AMOUNT_REQUIRED, fieldName + "은(는) 필수입니다.");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException(ErrorCode.ORDER_AMOUNT_INVALID, fieldName + "은(는) 0 이상이어야 합니다.");
        }
    }

    // ===== 테스트를 위한 ID 설정 메서드 (인메모리 DB용) =====
    public void setId(Long id) {
        this.id = id;
    }
}
