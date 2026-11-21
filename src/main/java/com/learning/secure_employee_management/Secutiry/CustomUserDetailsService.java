package com.learning.secure_employee_management.Secutiry;

import com.learning.secure_employee_management.Entity.EmployeeEntity;
import com.learning.secure_employee_management.Exception.EmployeeNotFoundException;
import com.learning.secure_employee_management.Repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    EmployeeRepository employeeRepository;
    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public UserDetails loadUserByUsername(String email)
    {
        EmployeeEntity employee=employeeRepository.findByEmail(email).orElseThrow(() ->
                new  EmployeeNotFoundException("User not found with email :"+email));

        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .roles(employee.getRole())
                .build();
    }

}

