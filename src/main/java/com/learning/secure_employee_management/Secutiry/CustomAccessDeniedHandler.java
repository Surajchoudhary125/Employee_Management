package com.learning.secure_employee_management.Secutiry;

import com.learning.secure_employee_management.DTO.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    public final ObjectMapper mapper;
    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper=mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException
    {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false,
                "You do not have permission to access this resource",
                     null
        );
        mapper.writeValue(response.getWriter(), apiResponse);
    }

}
