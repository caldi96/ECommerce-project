package io.hhplus.ECommerce.ECommerce_project.order.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.Orders;
import io.hhplus.ECommerce.ECommerce_project.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class OrderMemoryRepository implements OrderRepository {
    private final Map<Long, Orders> orderMap = new ConcurrentHashMap<>();
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public Orders save(Orders orders) {
        // ID가 없으면 Snowflake ID 생성
        if (orders.getId() == null) {
            orders.setId(idGenerator.nextId());
        }
        orderMap.put(orders.getId(), orders);
        return orders;
    }

    @Override
    public Optional<Orders> findById(Long id) {
        return Optional.ofNullable(orderMap.get(id));
    }

    @Override
    public List<Orders> findAll() {
        return new ArrayList<>(orderMap.values());
    }

    @Override
    public void deletedById(Long id) {
        orderMap.remove(id);
    }
}
