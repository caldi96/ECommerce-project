package io.hhplus.ECommerce.ECommerce_project.order.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.order.domain.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Orders save(Orders orders);

    Optional<Orders> findById(Long id);

    List<Orders> findAll();

    void deletedById(Long id);
}
