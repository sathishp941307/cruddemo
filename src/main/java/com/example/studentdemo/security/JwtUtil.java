package com.example.studentdemo.security;


import com.example.studentdemo.model.Role;
import com.example.studentdemo.model.User;
import com.example.studentdemo.repo.UserRepo;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    //secret key

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    //expiration time for validity of the token

    private final int EXPIRATION_TIME = 86400000; // 24 hours

    private UserRepo userRepo;

    public JwtUtil(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //generate token
    public String generateToken(String username) {

        Optional<User> user = userRepo.findByUsername(username);
        Set<Role> roles = user.get().getRoles();

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.stream().map(Role::getName).collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }


    //Extract user name

    public String extractUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //extreatc roles

    public Set<  String> extractRoles(String token) {
        String roles = Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String.class);
        return Set.of(roles.split(","));
    }

    //validate token

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token");
            return false;

        }

    }




}
