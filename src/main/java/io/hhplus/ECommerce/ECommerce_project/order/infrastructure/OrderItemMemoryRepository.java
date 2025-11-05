package io.hhplus.ECommerce.ECommerce_project.order.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.OrderItem;
import io.hhplus.ECommerce.ECommerce_project.order.domain.repository.OrderItemRepository;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class OrderItemMemoryRepository implements OrderItemRepository {
    private final Map<Long, OrderItem> orderItemMap = new ConcurrentHashMap<>();
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public OrderItem save(OrderItem orderItem) {
        // ID가 없으면 Snowflake ID 생성
        if (orderItem.getId() == null) {
            orderItem.setId(idGenerator.nextId());
        }
        orderItemMap.put(orderItem.getId(), orderItem);
        return orderItem;
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        return Optional.ofNullable(orderItemMap.get(id));
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemMap.values().stream()
                .filter(orderItem -> Objects.equals(orderItem.getOrderId(), orderId))
                .toList();
    }

    @Override
    public List<OrderItem> findAll() {
        return new ArrayList<>(orderItemMap.values());
    }

    @Override
    public void deleteById(Long id) {
        orderItemMap.remove(id);
    }
}
