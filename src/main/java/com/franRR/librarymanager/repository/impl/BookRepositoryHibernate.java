package com.franRR.librarymanager.repository.impl;

import com.franRR.librarymanager.HibernateUtil;
import com.franRR.librarymanager.model.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class BookRepositoryHibernate implements BookRepository {

    @Override
    public void save(Book book) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(book);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar libro", e);
        }
    }

    @Override
    public void update(Book book) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(book);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar libro", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) session.remove(book);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar libro", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Book.class, id));
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Book WHERE isbn = :isbn", Book.class)
                    .setParameter("isbn", isbn)
                    .uniqueResultOptional();
        }
    }

    @Override
    public List<Book> findByTitleOrAuthor(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // El operador LIKE permite buscar coincidencias parciales (ej: escribir "Don" y que encuentre "Don Quijote")
            return session.createQuery("FROM Book WHERE lower(title) LIKE :key OR lower(author) LIKE :key", Book.class)
                    .setParameter("key", "%" + keyword.toLowerCase() + "%")
                    .list();
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Book", Book.class).list();
        }
    }
}
