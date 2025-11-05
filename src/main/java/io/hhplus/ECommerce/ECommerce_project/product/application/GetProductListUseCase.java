package io.hhplus.ECommerce.ECommerce_project.product.application;

import io.hhplus.ECommerce.ECommerce_project.product.application.dto.ProductPageResult;
import io.hhplus.ECommerce.ECommerce_project.product.application.enums.ProductSortType;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductListUseCase {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductPageResult execute(Long categoryId, ProductSortType sortType, int page, int size) {
        // 1. Repository에서 필터링/정렬/페이징된 상품 조회
        List<Product> products = productRepository.findProducts(categoryId, sortType, page, size);

        // 2. 총 개수 조회 (페이징 메타데이터용)
        long totalElements = productRepository.countActiveProducts(categoryId);

        // 3. 결과 반환
        return new ProductPageResult(products, page, size, totalElements);
    }
}
