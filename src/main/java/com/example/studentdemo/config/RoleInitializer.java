package com.example.studentdemo.config;

import com.example.studentdemo.model.Role;
import com.example.studentdemo.repo.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepo roleRepo) {
        return args -> {
            if (roleRepo.findByName("USER").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("USER");
                roleRepo.save(userRole);
            }
            if (roleRepo.findByName("ADMIN").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepo.save(adminRole);
            }
        };
    }
}

