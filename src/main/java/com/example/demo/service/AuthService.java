package com.example.demo.service;

import com.example.demo.dto.SignupRequest;
import com.example.demo.model.PendingRegistration;
import com.example.demo.repository.pendingRegistrationRepository;
import com.example.demo.repository.userRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final userRepository userRepository;
    private final pendingRegistrationRepository pendingRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    public AuthService(
            userRepository userRepository,
            pendingRegistrationRepository pendingRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.pendingRepository = pendingRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    public String signup(SignupRequest request) {

       
        String email = request.getEmail()
                .toLowerCase()
                .trim();

       
        if (userRepository.existsByEmail(email)) {
            return "User with this email already exists";
        }

        
        pendingRepository.deleteByEmail(email);

        
        String otp = otpService.generateOtp();

      
        String hashedPassword =
                passwordEncoder.encode(request.getPassword());

        
        LocalDateTime expiry =
                LocalDateTime.now().plusMinutes(10);

       
        PendingRegistration pending =
                new PendingRegistration(
                        request.getName(),
                        email,
                        hashedPassword,
                        otp,
                        expiry
                );

        pendingRepository.save(pending);

        // Send OTP through Gmail
        emailService.sendOtp(email, otp);

        return "OTP sent successfully to your email";
    }
}