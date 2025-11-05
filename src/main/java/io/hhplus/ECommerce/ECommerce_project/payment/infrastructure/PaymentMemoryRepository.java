package io.hhplus.ECommerce.ECommerce_project.payment.infrastructure;

import io.hhplus.ECommerce.ECommerce_project.common.SnowflakeIdGenerator;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.entity.Payment;
import io.hhplus.ECommerce.ECommerce_project.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class PaymentMemoryRepository implements PaymentRepository {
    private final Map<Long, Payment> paymentMap = new ConcurrentHashMap<>();
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public Payment save(Payment payment) {
        // ID가 없으면 Snowflake ID 생성
        if (payment.getId() == null) {
            payment.setId(idGenerator.nextId());
        }
        paymentMap.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(paymentMap.get(id));
    }

    @Override
    public List<Payment> findByOrderId(Long orderId) {
        return paymentMap.values().stream()
                .filter(payment -> Objects.equals(payment.getOrderId(), orderId))
                .toList();
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(paymentMap.values());
    }

    @Override
    public void deleteById(Long id) {
        paymentMap.remove(id);
    }
}
