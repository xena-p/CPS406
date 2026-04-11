package edu.tmu.group67.scrum_development.auth.service;

import edu.tmu.group67.scrum_development.auth.model.entity.*;
import edu.tmu.group67.scrum_development.auth.repository.UserRepository;
import edu.tmu.group67.scrum_development.auth.model.dto.*;
import edu.tmu.group67.scrum_development.auth.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    public AuthResponse login(AuthRequest request) {
        User user = repo.findByEmail(request.email()).orElse(null);

        if (user == null) {
            throw new RuntimeException("Invalid email or password.");
        }

        if (user.isAccountLock()) {
            throw new RuntimeException("Account locked after too many failed login attempts.");
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (AuthenticationException e) {
            // Increment failed counter and lock if threshold reached
            user.setFailedLogins(user.getFailedLogins() + 1);
            if (user.getFailedLogins() >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLock(true);
            }
            repo.save(user);
            throw new RuntimeException("Invalid email or password.");
        }

        // Successful login — reset failed counter
        user.setFailedLogins(0);

        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refresh);
        repo.save(user);

        return AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }

    public AuthResponse refresh(String refreshToken) {
        String email = jwtService.extractEmail(refreshToken);
        User user = repo.findByEmail(email).orElseThrow();

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccess = jwtService.generateAccessToken(user);

        return AuthResponse.builder()
                .accessToken(newAccess)
                .refreshToken(refreshToken)
                .build();
    }
}
