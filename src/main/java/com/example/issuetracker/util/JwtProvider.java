package com.example.issuetracker.util;
import com.example.issuetracker.config.JwtProperties;
import com.example.issuetracker.entity.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import io.jsonwebtoken.*;


import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component

public class JwtProvider {

    private final JwtProperties jwtProperties;
    public JwtProvider(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }

    // JWT Token generation
    public String generateToken(User user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        // Create token
        Key key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(user.getUserName())
                .claim("roles", user.getRoles()
                        .stream()
                        .map(role -> role.getRoleType().name())
                        .collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();

    }

    public byte[] getSecretBytes(){
        return jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
    }

    public Key getSecretKey(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
