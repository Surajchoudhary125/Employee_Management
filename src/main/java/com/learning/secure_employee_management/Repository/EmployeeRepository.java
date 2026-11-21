package com.learning.secure_employee_management.Repository;

import com.learning.secure_employee_management.Entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Integer> {

    Optional<EmployeeEntity> findByEmail(String email);
}
