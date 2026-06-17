package com.franRR.librarymanager.repository;

import com.franRR.librarymanager.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void save(Book book);
    void update(Book book);
    void delete(Long id);
    Optional<Book> findById(Long id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByTitleOrAuthor(String keyword); // <- El buscador dinámico
    List<Book> findAll();
}
