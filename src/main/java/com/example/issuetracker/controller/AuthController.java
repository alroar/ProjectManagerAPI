package com.example.issuetracker.controller;

import com.example.issuetracker.dto.LoginDTO;
import com.example.issuetracker.dto.TokenDTO;
import com.example.issuetracker.dto.UserDTO;
import com.example.issuetracker.entity.RefreshToken;
import com.example.issuetracker.entity.User;
import com.example.issuetracker.service.RefreshTokenService;
import com.example.issuetracker.service.UserService;
import com.example.issuetracker.util.JwtProvider;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
           String token = jwtProvider.generateToken(user);
           RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            TokenDTO tokenDTO = new TokenDTO(
                    token,
                    refreshToken.getTokenValue()
            );

           return ResponseEntity.ok(tokenDTO);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


}
