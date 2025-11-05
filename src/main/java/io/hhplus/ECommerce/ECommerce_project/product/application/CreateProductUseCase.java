package io.hhplus.ECommerce.ECommerce_project.product.application;

import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import io.hhplus.ECommerce.ECommerce_project.product.application.command.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public Product execute(CreateProductCommand command) {
        // 1. 도메인 생성
        Product product = Product.createProduct(
                command.name(),
                command.categoryId(),
                command.description(),
                command.price(),
                command.stock(),
                command.minOrderQuantity(),
                command.maxOrderQuantity()
        );

        // 2. 저장 후 반환
        return productRepository.save(product);
    }
}
