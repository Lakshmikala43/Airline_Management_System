package com.airline.controller;

import com.airline.dto.AuthDTOs;
import com.airline.security.SecurityUtils;
import com.airline.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.JwtResponse> login(@Valid @RequestBody AuthDTOs.LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDTOs.UserProfileResponse> register(@Valid @RequestBody AuthDTOs.RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @GetMapping("/profile")
    public ResponseEntity<AuthDTOs.UserProfileResponse> getProfile() {
        String email = SecurityUtils.getCurrentUserEmail()
            .orElseThrow(() -> new IllegalStateException("Unauthenticated request"));
        return ResponseEntity.ok(authService.getUserProfile(email));
    }
}
