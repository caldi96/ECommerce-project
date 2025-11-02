package io.hhplus.ECommerce.ECommerce_project.product.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.product.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);
}
