package com.franRR.librarymanager.repository.impl;

import com.franRR.librarymanager.HibernateUtil;
import com.franRR.librarymanager.model.Employee;
import com.franRR.librarymanager.repository.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryHibernate implements EmployeeRepository {


    @Override
    public void save(Employee employee) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al registrar al empleado", e);
        }

    }

    @Override
    public void update(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al registrar al empleado", e);
        }
    }

    @Override
    public void delete(Long id) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
            }
            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al eliminar al Empleado/a", e);
        }

    }

    @Override
    public Optional<Employee> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, id);
            return Optional.ofNullable(employee);
        }

    }

    @Override
    public List<Employee> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM Employee", Employee.class).list();
        }
    }

    @Override
    public Optional<Employee> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM Employee WHERE username = :username", Employee.class)
                    .setParameter("username", username)
                    .uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
