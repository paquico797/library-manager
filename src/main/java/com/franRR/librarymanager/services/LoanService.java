package com.franRR.librarymanager.services;
import com.franRR.librarymanager.model.Book;
import com.franRR.librarymanager.model.Loan;
import com.franRR.librarymanager.model.User;

import java.util.List;


public interface LoanService {

    void registerLoan(Book book, User user);

    void returnBook(Loan loan);

    List<Loan> getActiveLoans();

    List<Loan> findLoansByUserId(Long id);

    List<Loan> findAll();
}
