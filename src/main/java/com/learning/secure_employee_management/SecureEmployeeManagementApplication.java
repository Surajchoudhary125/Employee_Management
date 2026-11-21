package com.learning.secure_employee_management;

import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToStdout;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureEmployeeManagementApplication {

	public static void main(String[] args) {
		IO.println("Application started...");
		SpringApplication.run(SecureEmployeeManagementApplication.class, args);
	}

}
