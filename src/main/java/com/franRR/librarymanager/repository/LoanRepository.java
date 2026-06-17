package com.franRR.librarymanager.repository;

import com.franRR.librarymanager.model.Loan;
import java.util.List;

public interface LoanRepository {

    void save(Loan loan);
    void update(Loan loan);
    List<Loan> findActiveLoans(); // <- Préstamos sin devolver actuales
    List<Loan> findLoansByUserId(Long userId);
    List<Loan> findAll();
}
