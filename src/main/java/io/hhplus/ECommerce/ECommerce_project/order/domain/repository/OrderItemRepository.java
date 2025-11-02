package io.hhplus.ECommerce.ECommerce_project.order.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {

    OrderItem save(OrderItem orderItem);

    Optional<OrderItem> findById(Long id);

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findAll();

    void deleteById(Long id);
}
