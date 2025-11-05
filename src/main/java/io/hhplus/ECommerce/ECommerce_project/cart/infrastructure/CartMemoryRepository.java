package io.hhplus.ECommerce.ECommerce_project.cart.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.cart.domain.entity.Cart;
import io.hhplus.ECommerce.ECommerce_project.cart.domain.repository.CartRepository;
import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class CartMemoryRepository implements CartRepository {
    private final Map<Long, Cart> cartMap = new ConcurrentHashMap<>();
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
    public List<Cart> findByUserId(Long userId) {
        return cartMap.values().stream()
                .filter(cart -> cart.isSameUser(userId))
                .toList();
    }

    @Override
    public Optional<Cart> findByUserIdAndProductId(Long userId, Long productId) {
        return cartMap.values().stream()
                .filter(cart -> cart.isSameUser(userId)
                        && cart.isSameProduct(productId))
                .findFirst();
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
