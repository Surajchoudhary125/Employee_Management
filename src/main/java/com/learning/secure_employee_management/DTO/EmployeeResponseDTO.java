package com.learning.secure_employee_management.DTO;

public class EmployeeResponseDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String department;
    private String role;
    public EmployeeResponseDTO() {}
public EmployeeResponseDTO(int id, String firstName, String lastName, String email, String address, String department, String role) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;

    this.address = address;
    this.department = department;
    this.role = role;

}

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
