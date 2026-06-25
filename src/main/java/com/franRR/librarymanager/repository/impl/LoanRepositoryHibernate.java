package com.franRR.librarymanager.repository.impl;

import com.franRR.librarymanager.HibernateUtil;
import com.franRR.librarymanager.model.Loan;
import com.franRR.librarymanager.repository.LoanRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class LoanRepositoryHibernate implements LoanRepository {

    @Override
    public void save(Loan loan) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(loan);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al registrar préstamo", e);
        }
    }

    @Override
    public void update(Loan loan) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(loan);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al gestionar devolución", e);
        }
    }

    @Override
    public List<Loan> findActiveLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Añadido JOIN FETCH de book y user conjuntamente
            return session.createQuery("FROM Loan l JOIN FETCH l.book JOIN FETCH l.user WHERE l.returnDate IS NULL", Loan.class).list();
        }
    }

    @Override
    public List<Loan> findLoansByUserId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // El JOIN FETCH l.book es obligatorio aquí para evitar que el libro venga vacío (proxy)
            return session.createQuery(
                            "FROM Loan l JOIN FETCH l.book WHERE l.user.id = :userId", Loan.class)
                    .setParameter("userId", id)
                    .list();
        }
    }

    public List<Loan> getLoansByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Loan l JOIN FETCH l.book WHERE l.user.id = :userId", Loan.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    @Override
    public List<Loan> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Añadimos JOIN FETCH l.book aquí también por seguridad
            return session.createQuery("FROM Loan l JOIN FETCH l.book", Loan.class).list();
        }
    }
}
