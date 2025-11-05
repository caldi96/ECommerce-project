package io.hhplus.ECommerce.ECommerce_project.product.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.product.application.enums.ProductSortType;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

    /**
     * 활성화된 상품 목록 조회 (페이징, 카테고리 필터링, 정렬)
     */
    List<Product> findProducts(Long categoryId, ProductSortType sortType, int page, int size);

    /**
     * 활성화된 상품 총 개수 (페이징 메타데이터용)
     */
    long countActiveProducts(Long categoryId);
}
