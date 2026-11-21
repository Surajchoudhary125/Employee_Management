package com.learning.secure_employee_management.Mapper;

import com.learning.secure_employee_management.DTO.EmployeeRequestDTO;
import com.learning.secure_employee_management.DTO.EmployeeResponseDTO;
import com.learning.secure_employee_management.Entity.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeEntity toEntity(EmployeeRequestDTO employeeRequestDTO) {
        EmployeeEntity employeeEntity=new EmployeeEntity();
        employeeEntity.setFirstName(employeeRequestDTO.getFirstName());
        employeeEntity.setLastName(employeeRequestDTO.getLastName());
        employeeEntity.setEmail(employeeRequestDTO.getEmail());
        employeeEntity.setPassword(employeeRequestDTO.getPassword());
        employeeEntity.setDepartment(employeeRequestDTO.getDepartment());
        employeeEntity.setRole(employeeRequestDTO.getRole());
        employeeEntity.setAddress(employeeRequestDTO.getAddress());
        return employeeEntity;
    }

    public EmployeeResponseDTO toDTO(EmployeeEntity employeeEntity) {
    EmployeeResponseDTO employeeResponseDTO=new EmployeeResponseDTO();
    employeeResponseDTO.setId(employeeEntity.getId());
    employeeResponseDTO.setFirstName(employeeEntity.getFirstName());
    employeeResponseDTO.setLastName(employeeEntity.getLastName());
    employeeResponseDTO.setEmail(employeeEntity.getEmail());
    employeeResponseDTO.setRole(employeeEntity.getRole());
    employeeResponseDTO.setAddress(employeeEntity.getAddress());
    employeeResponseDTO.setDepartment(employeeEntity.getDepartment());
    return employeeResponseDTO;
    }
}
