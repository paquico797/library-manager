package com.franRR.librarymanager.services;
import com.franRR.librarymanager.model.Book;
import com.franRR.librarymanager.model.Loan;
import java.util.List;


public interface LoanService {

    void registerLoan(Book book);

    void returnBook(Loan loan);

    List<Loan> getActiveLoans();
}
