package io.hhplus.ECommerce.ECommerce_project.product.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ProductMemoryRepository implements ProductRepository {
    private final Map<Long, Product> productMap = new HashMap<>();
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public Product save(Product product) {
        // ID가 없으면 Snowflake ID 생성
        if (product.getId() == null) {
            product.setId(idGenerator.nextId());
        }
        productMap.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }

    @Override
    public void deleteById(Long id) {
        productMap.remove(id);
    }
}
