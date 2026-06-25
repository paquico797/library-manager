package com.franRR.librarymanager.services.impl;

import com.franRR.librarymanager.HibernateUtil;
import com.franRR.librarymanager.model.Book;
import com.franRR.librarymanager.model.Loan;
import com.franRR.librarymanager.model.User;
import com.franRR.librarymanager.repository.BookRepository;
import com.franRR.librarymanager.repository.LoanRepository;
import com.franRR.librarymanager.repository.impl.BookRepositoryHibernate;
import com.franRR.librarymanager.repository.impl.LoanRepositoryHibernate;
import com.franRR.librarymanager.services.LoanService;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository = new LoanRepositoryHibernate();
    private final BookRepository bookRepository = new BookRepositoryHibernate();

    @Override
    public void registerLoan(Book book, User user) {

        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("¡Alerta! No quedan ejemplares disponibles de: " + book.getTitle());
        }


        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.update(book);


        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(15);


        Loan newLoan = new Loan(book, today, dueDate);
        newLoan.setBook(book);
        newLoan.setUser(user);
        newLoan.setLoanDate(today);
        newLoan.setDueDate(dueDate);

        loanRepository.save(newLoan);
    }

    @Override
    public void returnBook(Loan loan) {

        loan.setReturnDate(LocalDate.now());
        loanRepository.update(loan);


        Book book = loan.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.update(book);
    }

    @Override
    public List<Loan> getActiveLoans() {
        return loanRepository.findActiveLoans();
    }

    @Override
    public List<Loan> findLoansByUserId(Long id) {

        return loanRepository.findLoansByUserId(id);
    }



    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }


}
