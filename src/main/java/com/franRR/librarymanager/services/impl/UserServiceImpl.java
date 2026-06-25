package com.franRR.librarymanager.services.impl;

import com.franRR.librarymanager.model.User;
import com.franRR.librarymanager.repository.impl.UserRepositoryHibernate;
import com.franRR.librarymanager.services.UserService;
import com.franRR.librarymanager.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    private final UserRepository userRepository = new UserRepositoryHibernate();

    public void save(User user) {

        java.util.Optional<User> existingUser = userRepository.findByDni(user.getDni());


        if (existingUser.isPresent() && (user.getId() == null || !existingUser.get().getId().equals(user.getId()))) {
            throw new IllegalStateException("Ya existe un cliente registrado con el DNI: " + user.getDni());
        }


        if (user.getId() == null) {
            userRepository.save(user);
        } else {
            userRepository.update(user);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser nulo.");
        }
        userRepository.delete(id);
    }


}