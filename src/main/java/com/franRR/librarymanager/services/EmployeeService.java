package com.franRR.librarymanager.services;

import com.franRR.librarymanager.model.Employee;

import java.util.List;

public interface EmployeeService {
    void save(Employee employee);
    List<Employee> findAll();
    void delete(Long id);
    Employee findByUsername(String username);
}
