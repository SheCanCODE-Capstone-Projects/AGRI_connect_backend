package com.scc.Agriconnect.service;

import com.scc.Agriconnect.dto.*;
import com.scc.Agriconnect.entity.*;
import com.scc.Agriconnect.Exception.*;
import com.scc.Agriconnect.integration.EmailService;
import com.scc.Agriconnect.repository.*;
import com.scc.Agriconnect.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CooperativeRepository cooperativeRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        if (cooperativeRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new IllegalArgumentException("A cooperative with this registration number already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(RoleType.PRESIDENT)
                .status(User.UserStatus.ACTIVE)
                .cooperative(null)
                .build();
        user = userRepository.save(user);

        Cooperative cooperative = Cooperative.builder()
                .name(request.getCooperativeName())
                .registrationNumber(request.getRegistrationNumber())
                .province(request.getProvince())
                .district(request.getDistrict())
                .sector(request.getSector())
                .contactInfo(request.getContactInfo())
                .description(request.getDescription())
                .status(Cooperative.CooperativeStatus.PENDING)
                .president(user)
                .build();
        cooperative = cooperativeRepository.save(cooperative);

        user.setCooperative(cooperative);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalStateException("Invalid email or password");
        }

        if (user.getCooperative() != null
                && user.getCooperative().getStatus() != Cooperative.CooperativeStatus.APPROVED) {

            if (user.getCooperative().getStatus() == Cooperative.CooperativeStatus.PENDING) {
                throw new IllegalArgumentException(
                        "Your cooperative registration is still pending approval. Please wait for a system admin to review it.");
            } else {
                throw new IllegalArgumentException(
                        "Your cooperative registration was rejected. Please contact support.");
            }
        }

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No account found with the provided email address"));

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(resetToken);

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new IllegalArgumentException("Password reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}