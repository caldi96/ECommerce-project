package io.hhplus.ECommerce.ECommerce_project.cart.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.repository.CartRepository;
import io.hhplus.ECommerce.ECommerce_project.category.domain.entity.Category;
import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CartMemoryRepository implements CartRepository {
    private final Map<Long, Cart> cartMap = new HashMap<>();
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public Cart save(Cart cart) {
        // ID가 없으면 Snowflake ID 생성
        if (cart.getId() == null) {
            cart.setId(idGenerator.nextId());
        }
        cartMap.put(cart.getId(), cart);
        return cart;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return Optional.ofNullable(cartMap.get(id));
    }

    @Override
    public List<Cart> findAll() {
        return new ArrayList<>(cartMap.values());
    }

    @Override
    public void deleteById(Long id) {
        cartMap.remove(id);
    }
}
