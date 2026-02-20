package com.example.issuetracker.controller;

import com.example.issuetracker.dto.LoginDTO;
import com.example.issuetracker.dto.TokenDTO;
import com.example.issuetracker.dto.UserDTO;
import com.example.issuetracker.entity.RefreshToken;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.exceptions.TokenNotFoundException;
import com.example.issuetracker.service.RefreshTokenService;
import com.example.issuetracker.service.UserService;
import com.example.issuetracker.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService, JwtProvider jwtProvider,
                          RefreshTokenService refreshTokenService){
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;

    }

    // Register ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Login ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDTO loginDTO){
        try {
           User user = userService.loginUser(loginDTO);
           RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
           String token = jwtProvider.generateToken(user, refreshToken);

           refreshTokenService.save(refreshToken);

           TokenDTO tokenDTO = new TokenDTO(
                    token,
                    refreshToken.getTokenValue()
           );

           return ResponseEntity.ok(tokenDTO);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // Logout ENDPOINT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws TokenNotFoundException {

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            return ResponseEntity.badRequest().body("No token provided");
        }

        String token = header.substring(7);

        Claims claims = Jwts.parser()
                .setSigningKey(jwtProvider.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String jwtId = claims.getId();
        refreshTokenService.revokeByJwtId(jwtId);

        return ResponseEntity.ok("Logged out successfully");
    }


}
