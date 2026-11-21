package org.example.practice7.controller;

import org.example.practice7.dto.LoginRequest;
import org.example.practice7.dto.LoginResponse;
import org.example.practice7.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthAndDemoController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @GetMapping("/public/hello")
    public ResponseEntity<String> publicHello() {
        return ResponseEntity.ok("Public endpoint: accessible without authentication.");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new LoginResponse(token, userDetails.getUsername(), roles));
    }

    @GetMapping("/profile")
    public ResponseEntity<String> userProfile(Authentication authentication) {
        return ResponseEntity.ok("Logged in as: " + authentication.getName() +
                " | Authorities: " + authentication.getAuthorities());
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Admin dashboard: access granted only for ROLE_ADMIN.");
    }

    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminReports() {
        return ResponseEntity.ok("Secret reports: protected via @PreAuthorize.");
    }
}