package com.franRR.librarymanager.services.impl;

import com.franRR.librarymanager.model.Category;
import com.franRR.librarymanager.services.CategoryService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public void save(Category category) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(category);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Category category){

        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(category);
            session.getTransaction().commit();
        }

    }

    @Override
    public List<Category> findAll() {
        try(Session session = sessionFactory.openSession()){
            return session.createQuery("FROM Category", Category.class).list();
        }
    }

}
