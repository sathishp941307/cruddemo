package com.example.studentdemo.controller;

import com.example.studentdemo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private JwtUtil jwtUtil;

    @Value("${role.admin}")
    private String roleAdmin = "ROLE_ADMIN";

    @Value("${role.user}")
    private String roleUser = "ROLE_USER";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController.roleAdmin = roleAdmin;
        userController.roleUser = roleUser;
    }

    @Test
    void testGetProtectedDataWithValidAdminToken() {
        String token = "Bearer validAdminToken";
        String jwtToken = "validAdminToken";
        String username = "adminUser";
        Set<String> roles = new HashSet<>();
        roles.add(roleAdmin);

        when(jwtUtil.validateToken(jwtToken)).thenReturn(true);
        when(jwtUtil.extractUsername(jwtToken)).thenReturn(username);
        when(jwtUtil.extractRoles(jwtToken)).thenReturn(roles);

        ResponseEntity<String> response = userController.getProtectedData(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Welcome adminUser, here is your role-specific data: [ROLE_ADMIN]", response.getBody());

        verify(jwtUtil, times(1)).validateToken(jwtToken);
        verify(jwtUtil, times(1)).extractUsername(jwtToken);
        verify(jwtUtil, times(1)).extractRoles(jwtToken);
    }

    @Test
    void testGetProtectedDataWithValidUserToken() {
        String token = "Bearer validUserToken";
        String jwtToken = "validUserToken";
        String username = "regularUser";
        Set<String> roles = new HashSet<>();
        roles.add(roleUser);

        when(jwtUtil.validateToken(jwtToken)).thenReturn(true);
        when(jwtUtil.extractUsername(jwtToken)).thenReturn(username);
        when(jwtUtil.extractRoles(jwtToken)).thenReturn(roles);

        ResponseEntity<String> response = userController.getProtectedData(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Welcome regularUser, here is your role-specific data: [ROLE_USER]", response.getBody());

        verify(jwtUtil, times(1)).validateToken(jwtToken);
        verify(jwtUtil, times(1)).extractUsername(jwtToken);
        verify(jwtUtil, times(1)).extractRoles(jwtToken);
    }

    @Test
    void testGetProtectedDataWithInvalidTokenFormat() {
        String token = "InvalidToken";

        ResponseEntity<String> response = userController.getProtectedData(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized: Missing or invalid token format", response.getBody());

        verify(jwtUtil, never()).validateToken(anyString());
    }

    @Test
    void testGetProtectedDataWithInvalidToken() {
        String token = "Bearer invalidToken";
        String jwtToken = "invalidToken";

        when(jwtUtil.validateToken(jwtToken)).thenReturn(false);

        ResponseEntity<String> response = userController.getProtectedData(token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());

        verify(jwtUtil, times(1)).validateToken(jwtToken);
    }

    @Test
    void testGetProtectedDataWithUnauthorizedRole() {
        String token = "Bearer validToken";
        String jwtToken = "validToken";
        String username = "unauthorizedUser";
        Set<String> roles = new HashSet<>();

        when(jwtUtil.validateToken(jwtToken)).thenReturn(true);
        when(jwtUtil.extractUsername(jwtToken)).thenReturn(username);
        when(jwtUtil.extractRoles(jwtToken)).thenReturn(roles);

        ResponseEntity<String> response = userController.getProtectedData(token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied: You don't have the required role", response.getBody());

        verify(jwtUtil, times(1)).validateToken(jwtToken);
        verify(jwtUtil, times(1)).extractUsername(jwtToken);
        verify(jwtUtil, times(1)).extractRoles(jwtToken);
    }

    @Test
    void testGetProtectedDataThrowsException() {
        String token = "Bearer validToken";
        String jwtToken = "validToken";

        when(jwtUtil.validateToken(jwtToken)).thenThrow(new RuntimeException("Test exception"));

        ResponseEntity<String> response = userController.getProtectedData(token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid token: Test exception", response.getBody());

        verify(jwtUtil, times(1)).validateToken(jwtToken);
    }
}