package com.example.bookservice.services;

import com.example.bookservice.dto.responses.UserResponse;
import com.example.bookservice.entities.User;
import com.example.bookservice.mappers.UserMapper;
import com.example.bookservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;


    public UserResponse getUserInfo(User user) {
        return UserMapper.INSTANCE.toDto(user);
    }

    public List<UserResponse> getUsers() {
        final int defaultLimit = 100;
        final int defaultOffset = 0;

        return this.getUsers(defaultLimit, defaultOffset);
    }

    public List<UserResponse> getUsers(int limit, int offset) {
        final List<User> events = this._getUsers(limit, offset);

        return UserMapper.INSTANCE.toDtos(events);
    }

    private List<User> _getUsers(int limit, int offset) {
        final Page<User> eventsPage = userRepo.findAll(
                PageRequest.of(offset, limit, Sort.by(Sort.Order.asc("id"))));

        return eventsPage.toList();
    }

}
