package com.example.Task_Manager.Controller;

import com.example.Task_Manager.TokenUtil;
import com.example.Task_Manager.Model.User;
import com.example.Task_Manager.Repository.TaskRepository;
import com.example.Task_Manager.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/{username}")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private TaskRepository taskRepo;

    @PostMapping("/register")
    public String register(@RequestBody User user){
        repo.save(user);
        return "Registered Successfully!!";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        System.out.println("INPUT USERNAME: " + user.getUsername());
        System.out.println("INPUT PASSWORD: " + user.getPassword());

        Optional<User> optionalUser = repo.findByUsername(user.getUsername());

        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        User dbUser = optionalUser.get();

        System.out.println("DB USER: " + dbUser.getUsername());

        if (!dbUser.getPassword().equals(user.getPassword())) {
            return "Invalid password";
        }

        return tokenUtil.generateToken(dbUser.getUsername());
    }
}