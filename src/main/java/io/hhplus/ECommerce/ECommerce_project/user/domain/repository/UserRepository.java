package io.hhplus.ECommerce.ECommerce_project.user.domain.repository;

import io.hhplus.ECommerce.ECommerce_project.user.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);
}
