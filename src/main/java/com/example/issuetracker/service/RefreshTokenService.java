package com.example.issuetracker.service;


import com.example.issuetracker.entity.RefreshToken;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.TokenNotFoundException;
import com.example.issuetracker.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = RefreshToken.of(user);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) throws TokenNotFoundException {
        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new TokenNotFoundException("Token have expired");
        }

        return refreshToken;
    }

    public void revokeToken(RefreshToken token){
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    public RefreshToken save(RefreshToken refreshToken){
        return refreshTokenRepository.save(refreshToken);
    }

    public void revokeByJwtId(String jwtId) throws TokenNotFoundException {
        RefreshToken token = refreshTokenRepository.findByJwtId(jwtId)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }


}
