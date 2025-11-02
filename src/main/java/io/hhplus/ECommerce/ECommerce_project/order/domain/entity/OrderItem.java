package io.hhplus.ECommerce.ECommerce_project.order.domain.entity;

import io.hhplus.ECommerce.ECommerce_project.order.domain.enums.OrderItemStatus;
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
public class OrderItem {

    private Long id;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_id")
    // private Product product;
    private Long productId;
    // 나중에 JPA 연결 시
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "order_id")
    // private Order order;
    private Long orderId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    private OrderItemStatus status;
    private String reason;
    private LocalDateTime confirmedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime returnedAt;
    private LocalDateTime refundedAt;
    private LocalDateTime createdAt;
}
