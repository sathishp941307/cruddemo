package com.example.studentdemo.repo;

import com.example.studentdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

}
