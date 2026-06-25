package com.franRR.librarymanager.services;

import com.franRR.librarymanager.model.Category;

import java.util.List;

public interface CategoryService {

    void save(Category categpry);
    void delete(Category category);
    List<Category> findAll();
}
