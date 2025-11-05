package io.hhplus.ECommerce.ECommerce_project.product.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.product.application.enums.ProductSortType;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductMemoryRepository implements ProductRepository {
    private final Map<Long, Product> productMap = new ConcurrentHashMap<>();
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
        return Optional.ofNullable(productMap.get(id))
                .filter(p -> p.getDeletedAt() == null);  // 삭제되지 않은 상품만 반환
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }

    @Override
    public void deleteById(Long id) {
        productMap.remove(id);
    }

    @Override
    public List<Product> findProducts(Long categoryId, ProductSortType sortType, int page, int size) {
        // 1. 활성화되고 삭제되지 않은 상품만 필터링
        var stream = productMap.values().stream()
                .filter(Product::isActive)
                .filter(p -> p.getDeletedAt() == null);

        // 2. 카테고리 필터링 (카테고리 ID가 있는 경우)
        if (categoryId != null) {
            stream = stream.filter(p -> categoryId.equals(p.getCategoryId()));
        }

        // 3. 정렬
        Comparator<Product> comparator = getComparator(sortType);
        stream = stream.sorted(comparator);

        // 4. 페이징 (skip & limit)
        return stream
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveProducts(Long categoryId) {
        var stream = productMap.values().stream()
                .filter(Product::isActive)
                .filter(p -> p.getDeletedAt() == null);

        if (categoryId != null) {
            stream = stream.filter(p -> categoryId.equals(p.getCategoryId()));
        }

        return stream.count();
    }

    private Comparator<Product> getComparator(ProductSortType sortType) {
        return switch (sortType) {
            case POPULAR -> Comparator.comparing(Product::getSoldCount).reversed();
            case VIEWED -> Comparator.comparing(Product::getViewCount).reversed();
            case PRICE_LOW -> Comparator.comparing(Product::getPrice);
            case PRICE_HIGH -> Comparator.comparing(Product::getPrice).reversed();
            case LATEST -> Comparator.comparing(Product::getCreatedAt).reversed();
        };
    }
}
