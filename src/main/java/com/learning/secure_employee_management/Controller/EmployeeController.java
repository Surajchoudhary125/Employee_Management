package com.learning.secure_employee_management.Controller;

import com.learning.secure_employee_management.DTO.ApiResponse;
import com.learning.secure_employee_management.DTO.EmployeeRequestDTO;
import com.learning.secure_employee_management.DTO.EmployeeResponseDTO;
import com.learning.secure_employee_management.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
   EmployeeController(EmployeeService employeeService) {
       this.employeeService = employeeService;
   }

   @PostMapping
   public ResponseEntity<ApiResponse<EmployeeResponseDTO>> addEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
       return employeeService.addEmployee(employeeRequestDTO);
   }


   @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getAllEmployees() {
       return employeeService.getAllEmployees();
   }

   @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployeeById( @PathVariable Integer id) {
       return employeeService.getEmployeeById(id);
   }

   @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployeeByEmail( @PathVariable String email) {
       return employeeService.getEmployeeByEmail(email);
   }

   @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployee( @PathVariable Integer id, @RequestBody EmployeeRequestDTO employeeRequestDTO) {
       return employeeService.updateEmployee(id,employeeRequestDTO);
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee( @PathVariable Integer id) {
       return employeeService.deleteEmployee(id);
   }
}