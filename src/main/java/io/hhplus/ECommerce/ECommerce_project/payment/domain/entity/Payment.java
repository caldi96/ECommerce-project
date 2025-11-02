package io.hhplus.ECommerce.ECommerce_project.payment.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentMethod;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentStatus;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentType;
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
    private String transactionId;
    private String pgProvider;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime failedAt;
}
