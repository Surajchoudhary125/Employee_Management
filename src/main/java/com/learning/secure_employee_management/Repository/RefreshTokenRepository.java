package com.learning.secure_employee_management.Repository;

import com.learning.secure_employee_management.Entity.EmployeeEntity;
import com.learning.secure_employee_management.Entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);
    List<RefreshTokenEntity> findAllByEmployee(EmployeeEntity employee);

}
