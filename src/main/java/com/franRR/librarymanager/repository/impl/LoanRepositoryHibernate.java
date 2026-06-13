package com.franRR.librarymanager.repository.impl;

import com.franRR.librarymanager.HibernateUtil;
import com.franRR.librarymanager.model.Loan;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class LoanRepositoryHibernate implements LoanRepository{

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
            // Si returnDate IS NULL significa que el lector no ha devuelto el libro todavía
            return session.createQuery("FROM Loan WHERE returnDate IS NULL", Loan.class).list();
        }
    }

    @Override
    public List<Loan> findLoansByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Loan WHERE user.id = :userId", Loan.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    @Override
    public List<Loan> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Loan", Loan.class).list();
        }
    }
}
