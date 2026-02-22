package com.example.issuetracker.controller;

import com.example.issuetracker.dto.RefreshRequestDTO;
import com.example.issuetracker.dto.TokenDTO;
import com.example.issuetracker.entity.RefreshToken;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.TokenErrorException;
import com.example.issuetracker.repository.RefreshTokenRepository;
import com.example.issuetracker.service.RefreshTokenService;
import com.example.issuetracker.util.JwtProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    JwtProvider jwtProvider;

    public RefreshTokenController(RefreshTokenService refreshTokenService,
                                  RefreshTokenRepository refreshTokenRepository,
                                  JwtProvider jwtProvider){
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/refresh")
    public TokenDTO generateRefreshToken(@RequestBody RefreshRequestDTO request) throws Exception{
        String tokenValue = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new TokenErrorException("Token couldn't be found"));

        if(refreshToken.isRevoked()){
            throw new TokenErrorException("Refresh token is revoked");
        }

        refreshTokenService.verifyExpiration(refreshToken);
        refreshTokenService.revokeToken(refreshToken);

        User user = refreshToken.getUser();

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        String newJwt = jwtProvider.generateToken(user, newRefreshToken);
        refreshTokenRepository.save(newRefreshToken);

        return new TokenDTO(newJwt, newRefreshToken.getTokenValue());
    }
}
