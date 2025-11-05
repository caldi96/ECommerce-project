package io.hhplus.ECommerce.ECommerce_project.order.application.response;

import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.OrderItem;
import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.Orders;
import io.hhplus.ECommerce.ECommerce_project.order.domain.enums.OrderStatus;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.entity.Payment;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentMethod;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderFromCartResponse(
    Long orderId,
    Long userId,
    BigDecimal totalAmount,
    BigDecimal shippingFee,
    BigDecimal discountAmount,
    BigDecimal pointAmount,
    BigDecimal finalAmount,
    OrderStatus orderStatus,
    LocalDateTime orderedAt,
    Long paymentId,
    PaymentStatus paymentStatus,
    PaymentMethod paymentMethod,
    LocalDateTime paidAt,
    List<OrderItemResponse> orderItems
) {
    public static CreateOrderFromCartResponse from(Orders order, Payment payment, List<OrderItem> orderItems) {
        return new CreateOrderFromCartResponse(
            order.getId(),
            order.getUserId(),
            order.getTotalAmount(),
            order.getShippingFee(),
            order.getDiscountAmount(),
            order.getPointAmount(),
            order.getFinalAmount(),
            order.getStatus(),
            order.getCreatedAt(),
            payment.getId(),
            payment.getPaymentStatus(),
            payment.getPaymentMethod(),
            payment.getCompletedAt(),
            orderItems.stream()
                .map(OrderItemResponse::from)
                .toList()
        );
    }

    public record OrderItemResponse(
        Long orderItemId,
        Long productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
    ) {
        public static OrderItemResponse from(OrderItem orderItem) {
            return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getSubTotal()
            );
        }
    }
}