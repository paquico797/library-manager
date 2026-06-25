package com.franRR.librarymanager.services.impl;
import com.franRR.librarymanager.model.Book;
import com.franRR.librarymanager.services.BookService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class BookServiceImpl implements BookService {

    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void save(Book book) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(book);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Book book){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(book);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Book book){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.update(book);
            session.getTransaction().commit();

        }
    }

    @Override
    public List<Book> findAll() {
        try(Session session = sessionFactory.openSession()){
            return session.createQuery("FROM Book", Book.class).list();
        }
    }
}
