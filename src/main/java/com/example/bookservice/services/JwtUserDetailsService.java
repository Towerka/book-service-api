package com.example.bookservice.services;

import com.example.bookservice.entities.User;
import com.example.bookservice.repositories.UserRepository;
import com.example.bookservice.security.JwtTokenUtil;
import com.example.bookservice.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found by username " + username);
        }

        return new MyUserDetails(user);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found by email " + email);
        }

        return new MyUserDetails(user);

    }

    public User getUserFromHeaders(HttpHeaders headers) {

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

        assert token != null;

        String final_token = token.substring(7);

        String username = jwtTokenUtil.getUsernameFromToken(final_token);

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found by username " + username);
        }

        return user;
    }
}