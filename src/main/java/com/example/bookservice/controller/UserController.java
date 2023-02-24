package com.example.bookservice.controller;


import com.example.bookservice.dto.responses.UserResponse;
import com.example.bookservice.entities.User;
import com.example.bookservice.repositories.UserRepository;
import com.example.bookservice.services.JwtUserDetailsService;
import com.example.bookservice.services.UserService;
import com.example.bookservice.utils.MakeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @GetMapping("")
    public ResponseEntity<UserResponse> getUserInfo(@RequestHeader HttpHeaders headers) {
        User user = jwtUserDetailsService.getUserFromHeaders(headers);

        return MakeResponse.makeOkResponse(userService.getUserInfo(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) {

        final List<UserResponse> users = limit != null
                ? userService.getUsers(limit, offset)
                : userService.getUsers();

        return MakeResponse.makeOkResponse(users);
    }
}