package com.franRR.librarymanager.services;

import com.franRR.librarymanager.model.Category;

import java.util.List;

public interface CategoryService {

    void save(Category categpry);
    List<Category> findAll();
}
