package com.example.bookservice.services;

import com.example.bookservice.entities.User;
import com.example.bookservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.SequenceGenerator;
import java.util.List;
import java.util.Optional;

@Service()
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping
    public boolean logined(User user, BindingResult result, Model model){
        boolean loggined = false;
        User user1 = userRepository.getUserByEmail(user.getEmail()).orElseThrow(()-> new IllegalStateException("user doesn't exist"));
        System.out.print("User1 password :" + user1.getPassword() + " " + user.getPassword());
        System.out.print(user.getPassword());
        if(user1.getPassword().equals(user.getPassword())){
            loggined = true;
        }
        System.out.println(loggined);
        return loggined;
    }

    public void addUser(User user, BindingResult result, Model model){
        userRepository.save(user);
    }

    @GetMapping
    public User findById(Long id){
        User user =  userRepository.findById(id).orElseThrow(()-> new IllegalStateException(
                "student with id " + id + " doesn't exists"
        ));
        return user;
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    @GetMapping
    public List<User> getUsers(){
        return userRepository.findAll();
    }

}
