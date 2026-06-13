package com.franRR.librarymanager.repository.impl;

import com.franRR.librarymanager.HibernateUtil;
import com.franRR.librarymanager.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public class UserRepositoryHibernate implements UserRepository{

    @Override
    public void save(User user) {
        Transaction transaction = null; // Preparación de la transacción de seguridad
        // Abrimos la sesión de forma segura (se cierra sola al salir del try)
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Iniciamos la transacción en MySQL
            session.persist(user); // Hibernate traduce el objeto 'User' a un INSERT de SQL
            transaction.commit(); // Confirmamos los cambios de forma definitiva
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Si el DNI o Email ya existían, cancela todo para no duplicar
            throw new RuntimeException("Error al registrar el usuario", e);
        }
    }

    // --- MODIFICAR DATOS DE UN USUARIO EXISTENTE ---
    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user); // Busca al usuario por su ID y actualiza sus campos (teléfono, email, etc.)
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }

    // --- ELIMINAR UN USUARIO POR SU ID ---
    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Primero buscamos si el usuario existe en MySQL usando su ID (Clave primaria)
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user); // Si existe, le pide a MySQL que ejecute el DELETE
            }
            transaction.commit();
        } catch (Exception e) {
            // NOTA: Si el usuario tiene un préstamo activo en la tabla 'loans', saltará el rollback
            // automáticamente debido a la restricción ON DELETE RESTRICT que pusimos en MySQL.
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error al eliminar el usuario (comprueba si tiene préstamos activos)", e);
        }
    }

    // --- BUSCAR UN USUARIO POR SU ID DIRECTO ---
    @Override
    public Optional<User> findById(Long id) {
        // Al ser una lectura pura, no necesitamos abrir transacciones ni hacer commits
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id); // Busca de forma ultra rápida por la PK
            return Optional.ofNullable(user); // Lo envolvemos en un Optional seguro
        }
    }

    // --- BUSCAR UN USUARIO POR SU DNI ---
    @Override
    public Optional<User> findByDni(String dni) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usamos HQL para buscar por un atributo específico que no es la clave primaria
            // ':dni' es el parámetro seguro para evitar ataques informáticos de inyección SQL
            return session.createQuery("FROM User WHERE dni = :dni", User.class)
                    .setParameter("dni", dni) // Reemplaza ':dni' por el DNI real que pasamos por parámetro
                    .uniqueResultOptional(); // Devuelve el usuario si lo encuentra, o un Optional vacío si no existe
        }
    }

    // --- BUSCAR UN USUARIO POR SU CORREO ELECTRÓNICO ---
    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Consulta HQL idéntica a la anterior pero filtrando por la columna de email
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }

    // --- OBTENER EL LISTADO COMPLETO DE LECTORES ---
    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // "FROM User" equivale al "SELECT * FROM users" de MySQL.
            // Hibernate transforma todas las filas devueltas en una cómoda lista de objetos Java.
            return session.createQuery("FROM User", User.class).list();
        }
    }
}
