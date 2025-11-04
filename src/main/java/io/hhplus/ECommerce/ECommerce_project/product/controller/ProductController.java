package io.hhplus.ECommerce.ECommerce_project.product.controller;

import io.hhplus.ECommerce.ECommerce_project.product.controller.request.CreateProductRequest;
import io.hhplus.ECommerce.ECommerce_project.product.controller.response.ProductResponse;
import io.hhplus.ECommerce.ECommerce_project.product.domain.repository.ProductRepository;
import io.hhplus.ECommerce.ECommerce_project.product.usecase.CreateProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ProductRepository productRepository;

    /**
     * 상품 등록
     */
    private ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        var product = createProductUseCase.execute(request.toCommand());
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        return ResponseEntity.ok(ProductResponse.from(product));
    }
}
