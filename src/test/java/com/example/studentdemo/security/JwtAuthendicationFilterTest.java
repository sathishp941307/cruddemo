//package com.example.studentdemo.security;
//
//import com.example.studentdemo.service.CustomUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class JwtAuthendicationFilterTest {
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private FilterChain filterChain;
//
//    @Mock
//    private UserDetails userDetails;
//
//    private JwtAuthendicationFilter jwtAuthendicationFilter;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        jwtAuthendicationFilter = new JwtAuthendicationFilter(jwtUtil, customUserDetailsService);
//    }
//
//    @Test
//    void doFilterInternal_WithValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
//        // Arrange
//        SecurityContextHolder.clearContext(); // Clear the SecurityContext before the test
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXRoaXNoIiwicm9sZXMiOiJVU0VSIiwiaWF0IjoxNzQ3MTk3NDc5LCJleHAiOjE3NDcyODM4Nzl9.BjnLJiij6d-Hp0WqkWOzy4h52pwoWmGjkRMLMeNFUIu1P4xXAHPnW1j4QE8Kxkwzq9MRUuTScZshKg-lwzGKNw";
//        String username = "testuser";
//
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//        when(jwtUtil.extractUsername(token)).thenReturn(username);
//        when(jwtUtil.validateToken(token)).thenReturn(true);
//        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
//        when(userDetails.getAuthorities()).thenReturn(null);
//
//        // Act
//        jwtAuthendicationFilter.doFilterInternal(request, response, filterChain);
//
//        // Assert
//        verify(customUserDetailsService, times(1)).loadUserByUsername(username);
//        verify(jwtUtil, times(1)).validateToken(token);
//        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
//
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
//
//    @Test
//    void doFilterInternal_WithInvalidToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
//        // Arrange
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXRoaXNoIiwicm9sZXMiOiJVU0VSIiwiaWF0IjoxNzQ3MTk3NDc5LCJleHAiOjE3NDcyODM4Nzl9.BjnLJiij6d-Hp0WqkWOzy4h52pwoWmGjkRMLMeNFUIu1P4xXAHPnW1j4QE8Kxkwzq9MRUuTScZshKg-lwzGKNw";
//
//        when(request.getHeader("Authorization")).thenReturn(token);
//        when(jwtUtil.extractUsername("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXRoaXNoIiwicm9sZXMiOiJVU0VSIiwiaWF0IjoxNzQ3MTk3NDc5LCJleHAiOjE3NDcyODM4Nzl9.BjnLJiij6d-Hp0WqkWOzy4h52pwoWmGjkRMLMeNFUIu1P4xXAHPnW1j4QE8Kxkwzq9MRUuTScZshKg-lwzGKNw")).thenReturn(null);
//
//        // Act
//        jwtAuthendicationFilter.doFilterInternal(request, response, filterChain);
//
//        // Assert
//       // assertNull(SecurityContextHolder.getContext().getAuthentication());
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
//
//    @Test
//    void doFilterInternal_WithoutToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
//        // Arrange
//        when(request.getHeader("Authorization")).thenReturn(null);
//
//        // Act
//        jwtAuthendicationFilter.doFilterInternal(request, response, filterChain);
//
//        // Assert
//        assertNull(SecurityContextHolder.getContext().getAuthentication());
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
//}