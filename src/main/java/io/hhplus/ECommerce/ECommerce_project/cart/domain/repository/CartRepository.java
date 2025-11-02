package io.hhplus.ECommerce.ECommerce_project.cart.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository {

    Cart save(Cart cart);

    Optional<Cart> findById(Long id);

    List<Cart> findAll();

    void deleteById(Long id);
}
