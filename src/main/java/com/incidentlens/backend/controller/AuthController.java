package com.incidentlens.backend.controller;


import com.incidentlens.backend.dto.LoginRequest;
import com.incidentlens.backend.dto.RegisterRequest;
import com.incidentlens.backend.entity.User;
import com.incidentlens.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> registerUser(@Valid  @RequestBody RegisterRequest request){

        User user = userService.registerUser(request.getName(), request.getEmail(), request.getPassword());

        return ResponseEntity.ok(Map.of(
                "message" , "User registered successfully",
        "id", user.getId(),
        "name" , user.getName(),
        "email" , user.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest request) {
        String token = userService.loginAndGetToken(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(Map.of(
                "message", "Login Successful",
                "token", token
        ));
    }

    @GetMapping("/me")
    public String me() {
        return "authenticated";
    }
}
