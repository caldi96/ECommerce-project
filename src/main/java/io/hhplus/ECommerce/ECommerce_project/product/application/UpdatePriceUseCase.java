package io.hhplus.ECommerce.ECommerce_project.product.application;

import io.hhplus.ECommerce.ECommerce_project.common.exception.ErrorCode;
import io.hhplus.ECommerce.ECommerce_project.common.exception.ProductException;
import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import io.hhplus.ECommerce.ECommerce_project.product.application.command.UpdatePriceCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePriceUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public Product execute(UpdatePriceCommand command) {
        // 1. 상품 조회
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        // 2. 가격 변경
        product.updatePrice(command.price());

        // 3. 변경사항 저장 및 반환
        return productRepository.save(product);
    }
}