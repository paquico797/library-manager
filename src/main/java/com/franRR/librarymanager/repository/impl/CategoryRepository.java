package com.franRR.librarymanager.repository.impl;

import com.franRR.librarymanager.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    void save(Category category);
    void update(Category category);
    void delete(Integer id);
    Optional<Category> findById(Integer id);
    Optional<Category> findByName(String name);
    List<Category> findAll();

}
