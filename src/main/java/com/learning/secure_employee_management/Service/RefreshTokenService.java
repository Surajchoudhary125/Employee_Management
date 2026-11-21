package com.learning.secure_employee_management.Service;

import com.learning.secure_employee_management.DTO.RefreshTokenRotationResult;
import com.learning.secure_employee_management.DTO.TokenResponseDTO;
import com.learning.secure_employee_management.Entity.EmployeeEntity;
import com.learning.secure_employee_management.Entity.RefreshTokenEntity;
import com.learning.secure_employee_management.Exception.InvalidRefreshTokenException;
import com.learning.secure_employee_management.Repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
public class RefreshTokenService {
   private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom=new SecureRandom();

    @Value("${jwt.refresh-expiration}")
    private long Refresh_expiry;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        }

    public String generateRefreshToken() {
        byte[] randomBytes  = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }



    public String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 algorithm unavailable", exception);
        }
    }

    // Create and save refresh token
    public String createRefreshToken(EmployeeEntity employee) {

        String rawToken = generateRefreshToken();

        RefreshTokenEntity entity = new RefreshTokenEntity();

        entity.setEmployee(employee);
        entity.setToken_hash(hashToken(rawToken));
        entity.setExpiry_date(
                Instant.now().plusMillis(Refresh_expiry));
        entity.setRevoked(false);

        refreshTokenRepository.save(entity);

        return rawToken;
    }

    @Transactional
    public RefreshTokenRotationResult rotateRefreshToken(String rawToken) {

        String tokenHash = hashToken(rawToken);

        RefreshTokenEntity existingToken =
                refreshTokenRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException(
                                        "Invalid refresh token"
                                )
                        );

        // Check whether token is revoked
        if (existingToken.isRevoked()) {
            throw new InvalidRefreshTokenException(
                    "Refresh token has been revoked"
            );
        }

        // Check expiration
        if (!existingToken.getExpiry_date().isAfter(Instant.now())) {
            throw new InvalidRefreshTokenException(
                    "Refresh token has expired"
            );
        }

        EmployeeEntity employee = existingToken.getEmployee();

        // Revoke old refresh token
        existingToken.setRevoked(true);

        refreshTokenRepository.save(existingToken);

        // Generate a new refresh token
        String newRefreshToken = createRefreshToken(employee);

        return new RefreshTokenRotationResult(
                employee,
                newRefreshToken
        );
    }
    @Transactional
    public void revokeRefreshToken(String rawToken) {

        String tokenHash = hashToken(rawToken);

        RefreshTokenEntity refreshToken =
                refreshTokenRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException(
                    "Refresh token already revoked"
            );
        }

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }
    @Transactional
    public void revokeAllRefreshTokens(EmployeeEntity employeeEntity) {
        List<RefreshTokenEntity> li= refreshTokenRepository.findAllByEmployee(employeeEntity);

        for(RefreshTokenEntity refreshTokenEntity : li) {
            refreshTokenEntity.setRevoked(true);
        }
        refreshTokenRepository.saveAll(li);

    }
//
//    public long getRefresh_expiry() {
//        return Refresh_expiry;
//    }
}
