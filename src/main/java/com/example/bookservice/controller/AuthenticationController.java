package com.example.bookservice.controller;

import com.example.bookservice.dto.requests.JwtRequest;
import com.example.bookservice.dto.requests.UserRequest;
import com.example.bookservice.services.AuthService;
import com.example.bookservice.utils.MakeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest user) {

        try {
            authService.saveUser(user);

            return MakeResponse.makeOkResponse("registered");
        } catch (Exception e) {
            return MakeResponse.makeConflictResponse(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtRequest authenticationRequest) {

        return authService.createAuthenticationToken(authenticationRequest);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody String token) {

        return authService.validateToken(token);
    }
}
