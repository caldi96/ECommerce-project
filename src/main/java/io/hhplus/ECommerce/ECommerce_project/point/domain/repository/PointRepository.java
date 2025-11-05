package io.hhplus.ECommerce.ECommerce_project.point.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.point.domain.entity.Point;

import java.util.List;
import java.util.Optional;

public interface PointRepository {

    Point save(Point point);

    Optional<Point> findById(Long id);

    List<Point> findAll();

    List<Point> findAvailablePointsByUserId(Long userId);

    void deleteById(Long id);
}
