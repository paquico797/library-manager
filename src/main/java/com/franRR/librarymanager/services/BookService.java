package com.franRR.librarymanager.services;

import com.franRR.librarymanager.model.Book;

import java.util.List;

public interface BookService {

    void save(Book book);
    void delete(Book book);
    List<Book> findAll();

    void update(Book book);
}
