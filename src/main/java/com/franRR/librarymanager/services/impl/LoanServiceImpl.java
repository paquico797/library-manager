package com.franRR.librarymanager.services.impl;

import com.franRR.librarymanager.model.Book;
import com.franRR.librarymanager.model.Loan;
import com.franRR.librarymanager.model.User;
import com.franRR.librarymanager.repository.BookRepository;
import com.franRR.librarymanager.repository.LoanRepository;
import com.franRR.librarymanager.repository.impl.BookRepositoryHibernate;
import com.franRR.librarymanager.repository.impl.LoanRepositoryHibernate;
import com.franRR.librarymanager.services.LoanService;

import java.time.LocalDate;
import java.util.List;

public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository = new LoanRepositoryHibernate();
    private final BookRepository bookRepository = new BookRepositoryHibernate();

    @Override
    public void registerLoan(Book book) {
        //  Validar si hay stock disponible del libro
        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("¡Alerta! No quedan ejemplares disponibles de: " + book.getTitle());
        }

        // Restamos una unidad del stock del libro en memoria
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        // Le pedimos al repositorio que actualice el nuevo stock en MySQL
        bookRepository.update(book);

        // Calculamos los tiempos (fecha hoy y vencimiento a 15 días)
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(15);

        // Creamos el objeto Loan con el constructor útil que configuramos
        Loan newLoan = new Loan(book, today, dueDate);

        // Lo persistimos de forma definitiva
        loanRepository.save(newLoan);
    }

    @Override
    public void returnBook(Loan loan) {
        //  Fijamos la fecha de devolución al día de hoy
        loan.setReturnDate(LocalDate.now());
        loanRepository.update(loan);

        //  Recuperamos el libro del préstamo y le devolvemos su stock (+1)
        Book book = loan.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.update(book);
    }

    @Override
    public List<Loan> getActiveLoans() {
        return loanRepository.findActiveLoans();
    }


}
