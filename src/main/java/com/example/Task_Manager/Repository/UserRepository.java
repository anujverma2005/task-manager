package com.example.Task_Manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Task_Manager.Model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}