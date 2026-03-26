package com.example.Task_Manager.Controller;

import com.example.Task_Manager.TokenUtil;
import com.example.Task_Manager.Model.User;
import com.example.Task_Manager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        repo.save(user);
        return ResponseEntity.ok("Registered Successfully!!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> optionalUser = repo.findByUsername(user.getUsername());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User dbUser = optionalUser.get();

        if (!dbUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        return ResponseEntity.ok(tokenUtil.generateToken(dbUser.getUsername()));
    }
}