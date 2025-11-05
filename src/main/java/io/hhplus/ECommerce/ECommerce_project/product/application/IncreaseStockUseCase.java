package io.hhplus.ECommerce.ECommerce_project.product.application;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ProductException;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import io.hhplus.ECommerce.ECommerce_project.product.application.command.IncreaseStockCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncreaseStockUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public Product execute(IncreaseStockCommand command) {
        // 1. 상품 조회
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        // 2. 재고 증가 (도메인 메서드 활용)
        product.increaseStock(command.quantity());

        // 3. 변경사항 저장 및 반환
        return productRepository.save(product);
    }
}