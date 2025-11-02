package io.hhplus.ECommerce.ECommerce_project.payment.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.payment.domain.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Long id);

    List<Payment> findByOrderId(Long orderId);

    List<Payment> findAll();

    void deleteById(Long id);
}
