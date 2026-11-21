package com.learning.secure_employee_management.Controller;

import com.learning.secure_employee_management.DTO.*;
import com.learning.secure_employee_management.Entity.EmployeeEntity;
import com.learning.secure_employee_management.Exception.EmployeeNotFoundException;
import com.learning.secure_employee_management.Repository.EmployeeRepository;
import com.learning.secure_employee_management.Service.EmployeeService;
import com.learning.secure_employee_management.Service.JWTService;
import com.learning.secure_employee_management.Service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmployeeRepository employeeRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JWTService jwtService,RefreshTokenService refreshTokenService,EmployeeRepository employeeRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String email = authentication.getName();
        EmployeeEntity employee=employeeRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Employee not found")
                );

        String token = jwtService.generateToken(email);
        long expiration=jwtService.getJWTexpiration();
        String refreshToken = refreshTokenService.createRefreshToken(employee);

        TokenResponseDTO response =new TokenResponseDTO();
        response.setRefreshToken(refreshToken);
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(expiration);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        RefreshTokenRotationResult result = refreshTokenService.rotateRefreshToken(request.getRefreshToken());

        String email = result.getEmployee().getEmail();

        String newAccessToken = jwtService.generateToken(email);
        long expiration=jwtService.getJWTexpiration();

        TokenResponseDTO response = new TokenResponseDTO();

        response.setAccessToken(newAccessToken);
        response.setRefreshToken(result.getRefreshToken());
        response.setTokenType("Bearer");
        response.setExpiresIn(expiration);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @Valid @RequestBody RefreshTokenRequestDTO request) {

        refreshTokenService.revokeRefreshToken(
                request.getRefreshToken()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Logout successful",
                        "User logged out successfully"
                )
        );
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<String>> logoutAll(Authentication authentication) {
        String email = authentication.getName();
        EmployeeEntity employeeEntity=employeeRepository.findByEmail(email).orElseThrow(()->new EmployeeNotFoundException("Employee not found with email" + email));
        refreshTokenService.revokeAllRefreshTokens(employeeEntity);

       ApiResponse<String> api= new ApiResponse<String>(
                true,
                "All token revoked successfully",
                "Token revoked successfully"
        );
       return ResponseEntity.ok(api);
    }
}
