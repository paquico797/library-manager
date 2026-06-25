package com.franRR.librarymanager.services.impl;

import com.franRR.librarymanager.HibernateUtil;
import com.franRR.librarymanager.model.Employee;
import com.franRR.librarymanager.repository.EmployeeRepository;
import com.franRR.librarymanager.repository.impl.EmployeeRepositoryHibernate;
import com.franRR.librarymanager.services.EmployeeService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    private final EmployeeRepository employeeRepository = new EmployeeRepositoryHibernate();

    @Override
    public void save(Employee employee) {

        Transaction transaction = null;

        // 1. Validar duplicados antes de abrir una transacción de guardado
        Employee existingEmployee = findByUsername(employee.getUsername());

        // Si ya existe un empleado con ese username y NO es el mismo que estamos editando (comparando IDs)
        if (existingEmployee != null && (employee.getId() == null || !existingEmployee.getId().equals(employee.getId()))) {
            throw new RuntimeException("El nombre de usuario '" + employee.getUsername() + "' ya está en uso.");
        }

        // 2. Proceder con el guardado si la validación es correcta
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Si el ID es nulo es un registro nuevo (save), si tiene ID es una actualización (merge/update)
            if (employee.getId() == null) {
                session.save(employee);
            } else {
                session.merge(employee);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el empleado: " + e.getMessage());
        }

    }

    @Override
    public List<Employee> findAll() {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Employee ", Employee.class).list();
        }

    }

    @Override
    public void delete(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo.");
        }
        employeeRepository.delete(id);

    }

    @Override
    public Employee findByUsername(String username) {
        Transaction transaction = null;
        Employee employee = null;

        // Abrimos la sesión de Hibernate de forma segura
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Creamos la consulta HQL buscando por el atributo 'username' de tu entidad Employee
            String hql = "FROM Employee WHERE username = :user";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("user", username);

            // Obtenemos el resultado único (o null si no se encuentra ninguno)
            employee = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return employee;
    }

}






