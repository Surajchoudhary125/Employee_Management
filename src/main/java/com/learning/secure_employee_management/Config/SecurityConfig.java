package com.learning.secure_employee_management.Config;

import com.learning.secure_employee_management.Filter.JWTAuthenticationFilter;
import com.learning.secure_employee_management.Secutiry.CustomAccessDeniedHandler;
import com.learning.secure_employee_management.Secutiry.CustomAuthenticationEntryPoint;
import com.learning.secure_employee_management.Secutiry.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    public final CustomAccessDeniedHandler customAccessDeniedHandler;
    public final CustomAuthenticationEntryPoint  customAuthenticationEntryPoint;
    public final JWTAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/api/employee").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.PUT,"/api/employee/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/employee/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET,"/api/employee/**").hasAnyRole("ADMIN","MANAGER","USER")
                        .requestMatchers(HttpMethod.POST, "/api/employee").hasAnyRole("ADMIN","MANAGER","USER")
                        .requestMatchers(HttpMethod.POST, "/api/auth/login","/api/auth/refresh","/api/auth/logout").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .httpBasic(basic ->
                        basic.authenticationEntryPoint(customAuthenticationEntryPoint))

                .httpBasic(basic -> basic.disable())

                .formLogin(form -> form.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService service,PasswordEncoder encoder) throws Exception {
        DaoAuthenticationProvider dao=new DaoAuthenticationProvider(service);
        dao.setPasswordEncoder(encoder);
        return dao;
    }

    @Bean
    public AuthenticationManager  authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
    