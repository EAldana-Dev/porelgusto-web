package com.neksbyte.porelgustoweb.controller;

import com.neksbyte.porelgustoweb.dto.auth.AuthResponse;
import com.neksbyte.porelgustoweb.dto.auth.LoginRequest;
import com.neksbyte.porelgustoweb.dto.auth.RegistroRequest;
import com.neksbyte.porelgustoweb.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
