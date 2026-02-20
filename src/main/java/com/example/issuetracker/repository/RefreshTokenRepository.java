package com.example.issuetracker.repository;

import com.example.issuetracker.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenValue(String tokenValue);

    boolean existsByJwtIdAndRevokedFalse(String jwtId);
    Optional<RefreshToken> findByJwtId(String jwtId);
}
