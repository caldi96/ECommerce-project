package io.hhplus.ECommerce.ECommerce_project.product.presentation;

import io.hhplus.ECommerce.ECommerce_project.product.application.enums.ProductSortType;
import io.hhplus.ECommerce.ECommerce_project.product.presentation.request.*;
import io.hhplus.ECommerce.ECommerce_project.product.presentation.response.PageResponse;
import io.hhplus.ECommerce.ECommerce_project.product.presentation.response.ProductResponse;
import io.hhplus.ECommerce.ECommerce_project.product.application.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductListUseCase getProductListUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final UpdatePriceUseCase updatePriceUseCase;
    private final IncreaseStockUseCase increaseStockUseCase;
    private final DecreaseStockUseCase decreaseStockUseCase;
    private final ActivateProductUseCase activateProductUseCase;
    private final DeactivateProductUseCase deactivateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        var createdProduct = createProductUseCase.execute(request.toCommand());
        return ResponseEntity.ok(ProductResponse.from(createdProduct));
    }

    /**
     * 상품 목록 조회(전체, 카테고리별, 정렬, 페이징)
     */
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getProductList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "LATEST") ProductSortType sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var result = getProductListUseCase.execute(categoryId, sortType, page, size);

        // Product -> ProductResponse 변환
        List<ProductResponse> content = ProductResponse.from(result.getProducts());

        // 페이징 응답 생성
        PageResponse<ProductResponse> pageResponse = PageResponse.of(
                content,
                result.getPage(),
                result.getSize(),
                result.getTotalElements()
        );

        return ResponseEntity.ok(pageResponse);
    }

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        var product = getProductUseCase.execute(id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /**
     * 상품 수정(전체)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        var updatedProduct = updateProductUseCase.execute(request.toCommand(id));
        return ResponseEntity.ok(ProductResponse.from(updatedProduct));
    }

    /**
     * 상품 가격 수정
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductResponse> updatePrice(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceRequest request
    ) {
        var product = updatePriceUseCase.execute(request.toCommand(id));
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /**
     * 재고 증가 (입고, 반품 등)
     */
    @PatchMapping("/{id}/stock/increase")
    public ResponseEntity<ProductResponse> increaseStock(
            @PathVariable Long id,
            @Valid @RequestBody IncreaseStockRequest request
    ) {
        var product = increaseStockUseCase.execute(request.toCommand(id));
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /**
     * 재고 감소 (파손, 폐기 등)
     */
    @PatchMapping("/{id}/stock/decrease")
    public ResponseEntity<ProductResponse> decreaseStock(
            @PathVariable Long id,
            @Valid @RequestBody DecreaseStockRequest request
    ) {
        var product = decreaseStockUseCase.execute(request.toCommand(id));
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /**
     * 상품 활성화
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<ProductResponse> activateProduct(@PathVariable Long id) {
        var product = activateProductUseCase.execute(id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /**
     * 상품 비활성화
     */
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable Long id) {
        var product = deactivateProductUseCase.execute(id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
