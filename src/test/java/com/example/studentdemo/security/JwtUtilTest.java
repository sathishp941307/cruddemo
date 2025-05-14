package com.example.studentdemo.security;

import com.example.studentdemo.model.Role;
import com.example.studentdemo.model.User;
import com.example.studentdemo.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    @Mock
    private UserRepo userRepo;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil(userRepo);

        // Mock userRepo to return a valid user
        String username = "testuser";
        Role role = new Role();
        role.setName("ROLE_USER");
        User user = new User();
        user.setUsername(username);
        user.setRoles(Set.of(role));

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // Arrange
        String username = "testuser";
        Role role = new Role();
        role.setName("ROLE_USER");
        User user = new User();
        user.setUsername(username);
        user.setRoles(Set.of(role));

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        String token = jwtUtil.generateToken(username);

        // Assert
        assertNotNull(token);
        verify(userRepo, times(1)).findByUsername(username);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        // Act
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void extractRoles_ShouldReturnCorrectRoles() {
        // Arrange
        String username = "testuser";
        Role role = new Role();
        role.setName("ROLE_USER");
        User user = new User();
        user.setUsername(username);
        user.setRoles(Set.of(role));

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        String token = jwtUtil.generateToken(username);

        // Act
        Set<String> roles = jwtUtil.extractRoles(token);

        // Assert
        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_USER"));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Arrange
        String invalidToken = "invalidToken";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }
}