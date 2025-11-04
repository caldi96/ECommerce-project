package io.hhplus.ECommerce.ECommerce_project.payment.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.PaymentException;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentMethod;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentStatus;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 정적 팩토리 메서드를 위한 private 생성자
public class Payment {

    private Long id;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "order_id")
    // private Orders order;
    private Long orderId;
    private BigDecimal amount;
    private PaymentType paymentType;        // 결제 / 환불
    private PaymentMethod paymentMethod;    // 카드, 계좌이체 등
    private PaymentStatus paymentStatus;    // PENDING, COMPLETED, FAILED, REFUNDED
    //private String transactionId;
    //private String pgProvider;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime failedAt;

    // ===== 정적 팩토리 메서드 =====

    /**
     * 새로운 결제 생성 (결제 시작)
     */
    public static Payment createPayment(Long orderId, BigDecimal amount, PaymentMethod paymentMethod) {
        validateAmount(amount);
        validateOrderId(orderId);

        LocalDateTime now = LocalDateTime.now();
        return new Payment(
            null,  // id는 저장 시 생성
            orderId,
            amount,
            PaymentType.PAYMENT,
            paymentMethod,
            PaymentStatus.PENDING,
            null,  // failureReason
            now,   // createdAt
            now,   // updatedAt
            null,  // completedAt
            null   // failedAt
        );
    }

    /**
     * 환불 생성
     */
    public static Payment createRefund(Long orderId, BigDecimal amount, PaymentMethod paymentMethod) {
        validateAmount(amount);
        validateOrderId(orderId);

        LocalDateTime now = LocalDateTime.now();
        return new Payment(
            null,
            orderId,
            amount,
            PaymentType.REFUND,
            paymentMethod,
            PaymentStatus.PENDING,
            null,
            now,
            now,
            null,
            null
        );
    }

    // ===== 비즈니스 로직 메서드 =====

    /**
     * 결제 완료 처리
     */
    public void complete() {
        if (this.paymentStatus == PaymentStatus.COMPLETED) {
            throw new PaymentException(ErrorCode.PAYMENT_ALREADY_COMPLETED);
        }

        if (this.paymentStatus == PaymentStatus.FAILED) {
            throw new PaymentException(ErrorCode.PAYMENT_CANNOT_COMPLETE_FAILED);
        }

        if (this.paymentStatus == PaymentStatus.REFUNDED) {
            throw new PaymentException(ErrorCode.PAYMENT_CANNOT_COMPLETE_REFUNDED);
        }

        LocalDateTime now = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.completedAt = now;
        this.updatedAt = now;
    }

    /**
     * 결제 실패 처리
     */
    public void fail(String reason) {
        if (this.paymentStatus == PaymentStatus.COMPLETED) {
            throw new PaymentException(ErrorCode.PAYMENT_CANNOT_FAIL_COMPLETED);
        }

        if (this.paymentStatus == PaymentStatus.FAILED) {
            throw new PaymentException(ErrorCode.PAYMENT_ALREADY_FAILED);
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new PaymentException(ErrorCode.PAYMENT_FAILURE_REASON_REQUIRED);
        }

        LocalDateTime now = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.failedAt = now;
        this.updatedAt = now;
    }

    /**
     * 환불 처리
     */
    public void refund() {
        // 결제 타입이 PAYMENT인 경우에만 환불 가능
        if (this.paymentType != PaymentType.PAYMENT) {
            throw new PaymentException(ErrorCode.PAYMENT_ONLY_PAYMENT_TYPE_CAN_REFUND);
        }

        if (this.paymentStatus == PaymentStatus.REFUNDED) {
            throw new PaymentException(ErrorCode.PAYMENT_ALREADY_REFUNDED);
        }

        if (this.paymentStatus != PaymentStatus.COMPLETED) {
            throw new PaymentException(ErrorCode.PAYMENT_ONLY_COMPLETED_CAN_REFUND);
        }

        LocalDateTime now = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.REFUNDED;
        this.updatedAt = now;
    }

    // ===== 상태 확인 메서드 =====

    /**
     * 결제 완료 여부
     */
    public boolean isCompleted() {
        return this.paymentStatus == PaymentStatus.COMPLETED;
    }

    /**
     * 결제 실패 여부
     */
    public boolean isFailed() {
        return this.paymentStatus == PaymentStatus.FAILED;
    }

    /**
     * 환불 완료 여부
     */
    public boolean isRefunded() {
        return this.paymentStatus == PaymentStatus.REFUNDED;
    }

    /**
     * 결제 대기 중 여부
     */
    public boolean isPending() {
        return this.paymentStatus == PaymentStatus.PENDING;
    }

    /**
     * 환불 가능 여부
     */
    public boolean canRefund() {
        return this.paymentType == PaymentType.PAYMENT
            && this.paymentStatus == PaymentStatus.COMPLETED;
    }

    // ===== Validation 메서드 =====

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new PaymentException(ErrorCode.PAYMENT_AMOUNT_REQUIRED);
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentException(ErrorCode.PAYMENT_AMOUNT_INVALID);
        }
    }

    private static void validateOrderId(Long orderId) {
        if (orderId == null) {
            throw new PaymentException(ErrorCode.PAYMENT_ORDER_ID_REQUIRED);
        }
    }

    // ===== 테스트를 위한 ID 설정 메서드 (인메모리 DB용) =====
    public void setId(Long id) {
        this.id = id;
    }
}
