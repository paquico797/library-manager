package com.franRR.librarymanager.services;

import com.franRR.librarymanager.model.User;
import java.util.List;

public interface UserService {
    void save(User user);
    List<User> findAll();
    void delete(Long id);

}