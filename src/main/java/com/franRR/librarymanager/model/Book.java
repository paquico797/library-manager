package com.franRR.librarymanager.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "books", schema = "library_manager_db")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false, length = 150)
    private String author;

    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "publication_year")
    private java.time.LocalDate publicationDate;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public java.time.LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(java.time.LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }


    public Book(String isbn, String title, String author, LocalDate publicationYear, Integer availableQuantity, Category category) {

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationYear;
        this.availableQuantity = availableQuantity;
        this.category = category;
    }

    public Book() {
    }
}