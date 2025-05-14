package com.example.studentdemo.config;

import com.example.studentdemo.security.JwtAuthendicationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private JwtAuthendicationFilter jwtAuthenticationFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void testSecurityFilterChainBean() throws Exception {
        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(null);
        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    void testPasswordEncoderBean() {
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(PasswordEncoder.class);
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        assertThat(authenticationManager).isNotNull();
    }

    @Test
    void testJwtAuthenticationFilterInjected() {
        assertThat(jwtAuthenticationFilter).isNotNull();
    }
}