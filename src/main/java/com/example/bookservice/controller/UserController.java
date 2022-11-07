package com.example.bookservice.controller;


import com.example.bookservice.entities.User;
import com.example.bookservice.repositories.UserRepository;
import com.example.bookservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {



    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("")
    public String start(){
        return "home";
    }

    @GetMapping("/books")
    public String getBooks(){
        return "books";
    }

    @GetMapping("/login")
    public String login(User user){
        return "login";
    }
//    @PostMapping("/login")
//    public String post_login(User user, BindingResult result, Model model){
//        boolean is_log = userService.logined(user, result, model);
//        System.out.println("AAAAAAAAAA" + is_log);
//        if (result.hasErrors()) {
//            System.out.println("EEEEEEEEEEEEEEEEEEEEEE");
//        }
//        if(is_log)
//            return "session";
//        return "redirect:/index";
//    }


    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "signup";
    }

    @PostMapping("/signup")
    public String addUser( User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }

        userService.addUser(user, result, model);
        return "redirect:/index";
    }
    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "index";
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);

        model.addAttribute("user", user);
        return "update-user";
    }
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id,  User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userService.addUser(user, result, model);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        userService.deleteUser(user);
        return "redirect:/index";
    }

    // additional CRUD methods
}