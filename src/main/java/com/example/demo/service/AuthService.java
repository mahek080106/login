package com.example.demo.service;
import com.example.demo.dto.LoginRequest;
import com.example.demo.model.user;
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

        emailService.sendOtp(email, otp);

        return "OTP sent successfully to your email";
    }

public String verifyOtp(String email, String otp) {

    email = email.toLowerCase().trim();

   
    PendingRegistration pending =
            pendingRepository.findByEmail(email)
                    .orElse(null);

   
    if (pending == null) {
        return "No pending registration found";
    }

  
    if (LocalDateTime.now().isAfter(pending.getOtpExpiry())) {

        pendingRepository.deleteByEmail(email);

        return "OTP has expired";
    }

   
    if (!pending.getOtp().equals(otp)) {
        return "Invalid OTP";
    }

    
    com.example.demo.model.user user =
            new com.example.demo.model.user(
                    pending.getName(),
                    pending.getEmail(),
                    pending.getPassword()
            );


    userRepository.save(user);

    
    pendingRepository.deleteByEmail(email);

    return "Registration successful";
        }
public String login(LoginRequest request) {

   
    String email = request.getEmail()
            .toLowerCase()
            .trim();

    user user =
            userRepository.findByEmail(email)
                    .orElse(null);

  
    if (user == null) {
        return "Invalid email or password";
    }

   
    boolean passwordMatches =
            passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword()
            );

    
    if (!passwordMatches) {
        return "Invalid email or password";
    }

    return "Login successful. Welcome "
            + user.getName();
}
}