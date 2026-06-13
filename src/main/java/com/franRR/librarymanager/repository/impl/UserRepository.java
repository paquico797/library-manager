package com.franRR.librarymanager.repository.impl;

import com.franRR.librarymanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);
    void update(User user);
    void delete(Long id);
    Optional<User> findById(Long id);
    Optional<User> findByDni(String dni);
    Optional<User> findByEmail(String email);
    List<User> findAll();
}
