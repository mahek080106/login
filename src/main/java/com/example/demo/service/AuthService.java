package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.model.user;
import com.example.demo.repository.userRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            userRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String signup(SignupRequest request) {
        String email = request.getEmail()
                .toLowerCase()
                .trim();

        if (userRepository.existsByEmail(email)) {
            return "User with this email already exists";
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        user newUser = new user(
                request.getName(),
                email,
                hashedPassword
        );

        userRepository.save(newUser);

        return "Signup successful";
    }

    public String login(LoginRequest request) {
        String email = request.getEmail()
                .toLowerCase()
                .trim();

        user user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return "Invalid email or password";
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            return "Invalid email or password";
        }

        return "Login successful. Welcome " + user.getName();
    }
}