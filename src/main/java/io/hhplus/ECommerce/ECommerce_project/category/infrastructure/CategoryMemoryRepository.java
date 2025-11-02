package io.hhplus.ECommerce.ECommerce_project.category.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.category.domain.entity.Category;
import io.hhplus.ECommerce.ECommerce_project.category.domain.repository.CategoryRepository;
import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CategoryMemoryRepository implements CategoryRepository {
    private final Map<Long, Category> categoryMap = new HashMap<>();
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public Category save(Category category) {
        // ID가 없으면 Snowflake ID 생성
        if (category.getId() == null) {
            category.setId(idGenerator.nextId());
        }
        categoryMap.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categoryMap.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categoryMap.values());
    }

    @Override
    public void deleteById(Long id) {
        categoryMap.remove(id);
    }
}
