package com.example.bookservice.services;

import com.example.bookservice.dto.requests.JwtRequest;
import com.example.bookservice.dto.requests.UserRequest;
import com.example.bookservice.dto.responses.JwtResponse;
import com.example.bookservice.entities.User;
import com.example.bookservice.repositories.UserRepository;
import com.example.bookservice.security.JwtTokenUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;
    @Lazy
    @Autowired
    private PasswordEncoder bcryptEncoderStart;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    public void saveUser(@RequestBody UserRequest user) throws Exception {
        save(user);
    }

    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        String token;
        JwtResponse response = null;
        UserDetails userDetails = null;
        try {

            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("wrong password or username");
        }
        boolean isMatches = passwordMatches(authenticationRequest.getPassword(), userDetails.getPassword());
        if (isMatches) {
            token = jwtTokenUtil.generateToken(userDetails);
            response = new JwtResponse(token, "user found");
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> validateToken(@RequestBody String pre_token) {
        JSONObject json = new JSONObject(pre_token);
        String token = String.valueOf(json.get("token"));
        try {
            if (jwtTokenUtil.isTokenExpired(token)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("token expired");
            }
        } catch (NullPointerException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("no authorization field");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"expired\"" + ":" + false + "}");
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private boolean passwordMatches(String passInput, String passDatabase) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(
                passInput,
                passDatabase
        );
    }

    public void save(UserRequest user) throws Exception {
        if (userRepo.existsByUsername(user.getUsername()))
            throw new Exception("Username taken");

        if (userRepo.existsByEmail(user.getEmail()))
            throw new Exception("User with this email exists");

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoderStart.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());

        userRepo.save(newUser);
    }
}
