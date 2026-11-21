package com.learning.secure_employee_management.DTO;

import com.learning.secure_employee_management.Entity.EmployeeEntity;

public class RefreshTokenRotationResult {

    private EmployeeEntity employee;
    private String refreshToken;

    public RefreshTokenRotationResult(
            EmployeeEntity employee,
            String refreshToken) {
        this.employee = employee;
        this.refreshToken = refreshToken;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}