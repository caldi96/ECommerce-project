package io.hhplus.ECommerce.ECommerce_project.coupon.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.coupon.domain.entity.Coupon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long id);

    Optional<Coupon> findByCode(String code);

    List<Coupon> findAll();

    /**
     * 현재 시점 기준으로 활성화된 쿠폰 조회
     * (isActive = true AND startDate <= now AND endDate >= now)
     */
    List<Coupon> findAllByIsActiveTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime now1, LocalDateTime now2);

    void deleteById(Long id);
}
