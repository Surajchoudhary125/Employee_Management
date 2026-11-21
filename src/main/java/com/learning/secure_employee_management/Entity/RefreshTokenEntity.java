package com.learning.secure_employee_management.Entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "RefreshToken")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Refresh_id;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(nullable = false)
    private Instant expiry_date;


    @Column(nullable = false)
    private boolean revoked=false;


    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Instant getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Instant expiry_date) {
        this.expiry_date = expiry_date;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public String getToken_hash() {
        return tokenHash;
    }

    public void setToken_hash(String token_hash) {
        this.tokenHash = token_hash;
    }

    public Long getId() {
        return Refresh_id;
    }

    public void setId(Long id) {
        this.Refresh_id = Refresh_id;
    }


}
