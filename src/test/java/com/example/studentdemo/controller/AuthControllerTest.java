package com.example.studentdemo.controller;

import com.example.studentdemo.DTO.RegisterRequest;
import com.example.studentdemo.model.Role;
import com.example.studentdemo.model.User;
import com.example.studentdemo.repo.RoleRepo;
import com.example.studentdemo.repo.UserRepo;
import com.example.studentdemo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("password123");
        registerRequest.setRoles(Set.of("ROLE_USER"));

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        when(roleRepo.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("existingUser");
        registerRequest.setPassword("password123");

        when(userRepo.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User already exists", response.getBody());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testRegisterRoleNotFound() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("password123");
        registerRequest.setRoles(Set.of("ROLE_NON_EXISTENT"));

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(roleRepo.findByName("ROLE_NON_EXISTENT")).thenReturn(Optional.empty());

        RuntimeException exception = new RuntimeException("Role not found: ROLE_NON_EXISTENT");

        try {
            authController.register(registerRequest);
        } catch (RuntimeException e) {
            assertEquals("Role not found: ROLE_NON_EXISTENT", e.getMessage());
        }

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testLoginUserSuccess() {
        User loginRequest = new User();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUtil.generateToken("testUser")).thenReturn("testJwtToken");

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testJwtToken", response.getBody());
        verify(jwtUtil, times(1)).generateToken("testUser");
    }

    @Test
    void testLoginInvalidCredentials() {
        User loginRequest = new User();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("wrongPassword");

        doThrow(new RuntimeException("Bad credentials")).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid username or password: Bad credentials", response.getBody());
        verify(jwtUtil, never()).generateToken(anyString());
    }
}