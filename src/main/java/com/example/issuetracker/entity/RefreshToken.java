package com.example.issuetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tokenValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private boolean revoked;
    private Instant creationDate;
    private Instant expiryDate;

    private String jwtId;

    public static RefreshToken of(User user){
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenValue(UUID.randomUUID().toString());
        token.setCreationDate(Instant.now());
        token.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60));
        token.setRevoked(false);
        return token;
    }



}
