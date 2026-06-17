package com.franRR.librarymanager.services;

import com.franRR.librarymanager.model.Book;

import java.util.List;

public interface BookService {

    void save(Book book);
    List<Book> findAll();
}
