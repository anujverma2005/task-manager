package com.example.Task_Manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.example.Task_Manager.Model.Task;
import com.example.Task_Manager.Model.User;
import com.example.Task_Manager.Repository.TaskRepository;
import com.example.Task_Manager.Repository.UserRepository;
import com.example.Task_Manager.TokenUtil;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TokenUtil tokenUtil;

    // ✅ Add Task
    @PostMapping("/add")
    public ResponseEntity<String> createTask(
            @RequestBody Task task,
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        task.setUsername(username);
        taskRepo.save(task);
        return ResponseEntity.ok("Task Added Successfully");
    }

    // ✅ Get All Tasks for logged-in user
    @GetMapping("/all")
    public ResponseEntity<?> getTasks(
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        List<Task> tasks = taskRepo.findByUsername(username);
        return ResponseEntity.ok(tasks);
    }

    // ✅ Admin - Get All Tasks
    @GetMapping("/admin")
    public ResponseEntity<?> adminTasks(
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        User user = optionalUser.get();
        if (!"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok(taskRepo.findAll());
    }

    // ✅ Delete Task
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (!taskRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        taskRepo.deleteById(id);
        return ResponseEntity.ok("Task Deleted Successfully");
    }

    // ✅ Update Task
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(
            @PathVariable Long id,
            @RequestBody Task updatedTask,
            @RequestHeader("Authorization") String token) {

        String username = tokenUtil.validateToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<Task> optionalTask = taskRepo.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Task task = optionalTask.get();
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        taskRepo.save(task);

        return ResponseEntity.ok("Task Updated Successfully");
    }
}