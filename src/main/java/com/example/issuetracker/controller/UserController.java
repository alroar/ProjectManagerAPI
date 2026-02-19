package com.example.issuetracker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    // Me ENDPOINT
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(){
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        List<String> roles = auth.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();

        return Map.of(
                "username", username,
                "roles", roles,
                "message", "Este es un endpoint protegido"

        );
    }

    // Admin ENDPOINT
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminOnly(){
        return "Solo ADMIN puede ver esto";
    }
}

