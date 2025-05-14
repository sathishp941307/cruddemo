package com.example.studentdemo.config;

import com.example.studentdemo.model.Role;
import com.example.studentdemo.repo.RoleRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

class RoleInitializerTest {

    @Test
    void testInitRoles() throws Exception {
        // Mock the RoleRepo
        RoleRepo roleRepo = Mockito.mock(RoleRepo.class);

        // Mock behavior for findByName
        when(roleRepo.findByName("USER")).thenReturn(Optional.empty());
        when(roleRepo.findByName("ADMIN")).thenReturn(Optional.empty());

        // Create an instance of RoleInitializer
        RoleInitializer roleInitializer = new RoleInitializer();

        // Get the CommandLineRunner bean
        CommandLineRunner runner = roleInitializer.initRoles(roleRepo);

        // Execute the runner
        runner.run();

        // Verify that roles were saved
        verify(roleRepo, times(1)).save(argThat(role -> "USER".equals(role.getName())));
        verify(roleRepo, times(1)).save(argThat(role -> "ADMIN".equals(role.getName())));
    }
}