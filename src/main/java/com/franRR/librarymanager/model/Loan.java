package com.franRR.librarymanager.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans", schema = "library_manager_db")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        if (this.returnDate != null) {
            return "Devuelto";
        }

        // Si la fecha límite (dueDate) es anterior al día de hoy, está vencido
        if (this.dueDate != null && this.dueDate.isBefore(java.time.LocalDate.now())) {
            return "Vencido";
        }

        return "Activo";
    }

    public Loan( Book book, LocalDate loanDate, LocalDate dueDate) {

        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public Loan() {
    }
}