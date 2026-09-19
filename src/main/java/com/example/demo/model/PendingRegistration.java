package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "pending_registrations")
public class PendingRegistration {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private String otp;
    private LocalDateTime otpExpiry;

    public PendingRegistration() {
    }

    public PendingRegistration(
            String name,
            String email,
            String password,
            String otp,
            LocalDateTime otpExpiry) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }
}