package io.hhplus.ECommerce.ECommerce_project.point.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.point.domain.entity.Point;
import io.hhplus.ECommerce.ECommerce_project.point.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class PointMemoryRepository implements PointRepository {
    private final Map<Long, Point> pointMap = new ConcurrentHashMap<>();
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
    public List<Point> findAvailablePointsByUserId(Long userId) {
        return pointMap.values().stream()
            .filter(point -> Objects.equals(point.getUserId(), userId))
            .filter(Point::isAvailable)  // 사용 가능한 포인트만
            .sorted(Comparator.comparing(Point::getCreatedAt))  // 생성일 기준 오름차순 정렬 (선입선출)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        pointMap.remove(id);
    }
}
