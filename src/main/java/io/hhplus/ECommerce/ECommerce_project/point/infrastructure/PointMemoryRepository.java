package io.hhplus.ECommerce.ECommerce_project.point.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.point.domain.entity.Point;
import io.hhplus.ECommerce.ECommerce_project.point.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class PointMemoryRepository implements PointRepository {
    private final Map<Long, Point> pointMap = new HashMap<>();
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public Point save(Point point) {
        // ID가 없으면 Snowflake ID 생성
        if (point.getId() == null) {
            point.setId(idGenerator.nextId());
        }
        pointMap.put(point.getId(), point);
        return point;
    }

    @Override
    public Optional<Point> findById(Long id) {
        return Optional.ofNullable(pointMap.get(id));
    }

    @Override
    public List<Point> findAll() {
        return new ArrayList<>(pointMap.values());
    }

    @Override
    public void deleteById(Long id) {
        pointMap.remove(id);
    }
}
