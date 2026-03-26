package com.example.Task_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import com.example.Task_Manager.Model.Task;
import com.example.Task_Manager.Model.User;
import com.example.Task_Manager.Repository.TaskRepository;
import com.example.Task_Manager.Repository.UserRepository;
import com.example.Task_Manager.TokenUtil;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping("/add")
    public String createTask(
            @RequestBody Task task,
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        task.setUsername(username);
        taskRepo.save(task);

        return "Task Added Successfully";
    }

    @GetMapping("/all")
    public Object getTasks(
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        return taskRepo.findByUsername(username);
    }

    @GetMapping("/admin")
    public Object adminTasks(
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);

        if (username == null) {
            return "Unauthorized";
        }

        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "Unauthorized";
        }

        User user = optionalUser.get();

        if (!"ADMIN".equals(user.getRole())) {
            return "Access Denied";
        }

        return taskRepo.findAll();
    }
}