package edu.tmu.group67.scrum_development.auth.controller;

import edu.tmu.group67.scrum_development.auth.model.dto.*;
import edu.tmu.group67.scrum_development.auth.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // @PostMapping("/register")
    // public AuthResponse register(@RequestBody RegisterRequest request) {
    //     return service.register(request);
    // }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return service.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        // Access the token using the record's accessor method: request.refreshToken()
        return service.refresh(request.refreshToken());
    }
}
