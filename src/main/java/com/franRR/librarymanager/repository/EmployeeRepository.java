package com.franRR.librarymanager.repository;

import com.franRR.librarymanager.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    void save(Employee employee);
    void update(Employee employee);
    void delete(Long id);
    Optional<Employee> findById(Long id);

    List<Employee> findAll();
    Optional<Employee> findByUsername(String username);
}
