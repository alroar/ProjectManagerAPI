package com.example.issuetracker.service;


import com.example.issuetracker.entity.RefreshToken;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.TokenErrorException;
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

    public RefreshToken verifyExpiration(RefreshToken refreshToken) throws TokenErrorException {
        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new TokenErrorException("Token have expired");
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

    public void revokeByJwtId(String jwtId) throws TokenErrorException {
        RefreshToken token = refreshTokenRepository.findByJwtId(jwtId)
                .orElseThrow(() -> new TokenErrorException("Token not found"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }


}
