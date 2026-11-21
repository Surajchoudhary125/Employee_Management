package com.learning.secure_employee_management.Service;

import com.learning.secure_employee_management.DTO.ApiResponse;
import com.learning.secure_employee_management.DTO.EmployeeRequestDTO;
import com.learning.secure_employee_management.DTO.EmployeeResponseDTO;
import com.learning.secure_employee_management.Entity.EmployeeEntity;
import com.learning.secure_employee_management.Exception.DuplicateEmailException;
import com.learning.secure_employee_management.Exception.EmployeeNotFoundException;
import com.learning.secure_employee_management.Mapper.EmployeeMapper;
import com.learning.secure_employee_management.Repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.passwordEncoder = passwordEncoder;

    }

    public boolean emailExists(String email) {
        return employeeRepository.findByEmail(email).isPresent();
    }
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> addEmployee(EmployeeRequestDTO employeeRequestDTO) {

        if(emailExists(employeeRequestDTO.getEmail())) {
            throw new DuplicateEmailException("Email id already exists");
        }

        EmployeeEntity entity=employeeMapper.toEntity(employeeRequestDTO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));


        EmployeeResponseDTO employeeResponseDTO=employeeMapper.toDTO(employeeRepository.save(entity));
        ApiResponse<EmployeeResponseDTO> apiResponse=new ApiResponse<>(
                true,
                "Employee Created Successfully",
                employeeResponseDTO
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getAllEmployees() {
       List<EmployeeResponseDTO> list=employeeRepository.findAll().stream().map(employeeMapper::toDTO).toList();
       ApiResponse<List<EmployeeResponseDTO>> apiResponse=new ApiResponse<>(
               true,
               "All Employees Successfully",
               list
       );
       return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }



    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployeeById(Integer id) {
       EmployeeEntity entity= employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("Employee not found"));
        EmployeeResponseDTO data= employeeMapper.toDTO(entity);
       ApiResponse<EmployeeResponseDTO> response=new ApiResponse<>(
               true,
               "Employee Fetch Successfully",
               data

       );
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }



    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployeeByEmail(String email) {
        EmployeeEntity employeeEntity=employeeRepository.findByEmail(email).orElseThrow(()->new EmployeeNotFoundException("Employee not found with email" + email));
        EmployeeResponseDTO data=employeeMapper.toDTO(employeeEntity);
        ApiResponse<EmployeeResponseDTO> response=new ApiResponse<>(
                true,
                "Employee Fetch Successfully",
                data
        );
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }



    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployee(Integer id,EmployeeRequestDTO employeeRequestDTO) {
        EmployeeEntity employee = employeeRepository.findById(id).
                orElseThrow(()->
                        new EmployeeNotFoundException("Employee not found..")
        );
        EmployeeEntity employeeEntity=employeeMapper.toEntity(employeeRequestDTO);

        if(employeeEntity.getEmail()!=null)
         employee.setEmail(employeeEntity.getEmail());
        if(employeeEntity.getDepartment()!=null)
         employee.setDepartment(employeeEntity.getDepartment());
        if(employeeEntity.getAddress()!=null)
         employee.setAddress(employeeEntity.getAddress());

        EmployeeResponseDTO employeeResponseDTO=employeeMapper.toDTO(employeeRepository.save(employee));
        ApiResponse<EmployeeResponseDTO> response=new ApiResponse<>(
                true,
                "Employee data update successfully",
                employeeResponseDTO
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(Integer id) {

        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        employeeRepository.delete(employee);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Employee deleted successfully",
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
