package com.scc.Agriconnect.service;

import com.scc.Agriconnect.dto.*;
import com.scc.Agriconnect.entity.*;
import com.scc.Agriconnect.integration.EmailService;
import com.scc.Agriconnect.repository.*;
import com.scc.Agriconnect.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String PRESIDENT_ROLE = "PRESIDENT";

    private final UserRepository userRepository;
    private final CooperativeRepository cooperativeRepository;
    private final RoleRepository roleRepository;
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

        Role presidentRole = roleRepository.findByName(PRESIDENT_ROLE)
                .orElseThrow(() -> new IllegalStateException("PRESIDENT role not seeded"));

        User user = userRepository.save(User.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(presidentRole)
                .build());

        Cooperative cooperative = cooperativeRepository.save(Cooperative.builder()
                .name(request.getCooperativeName())
                .registrationNumber(request.getRegistrationNumber())
                .province(request.getProvince())
                .district(request.getDistrict())
                .sector(request.getSector())
                .contactInfo(request.getContactInfo())
                .description(request.getDescription())
                .president(user)
                .build());

        user.setCooperative(cooperative);
        userRepository.save(user);

        try {
            emailService.sendRegistrationConfirmation(
                    user.getEmail(), user.getFullName(), cooperative.getName());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send registration confirmation email: " + e.getMessage());
        }

        return buildAuthResponse(user, cooperative);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Cooperative cooperative = user.getCooperative();
        if (cooperative != null && cooperative.getStatus() != Cooperative.CooperativeStatus.APPROVED) {
            throw new IllegalStateException(
                    cooperative.getStatus() == Cooperative.CooperativeStatus.PENDING
                            ? "Your cooperative registration is still pending approval"
                            : "Your cooperative registration was rejected");
        }

        return buildAuthResponse(user, cooperative);
    }

    private AuthResponse buildAuthResponse(User user, Cooperative cooperative) {
        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user))
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .cooperativeId(cooperative != null ? cooperative.getCooperativeId() : null)
                .cooperativeStatus(cooperative != null ? cooperative.getStatus().name() : null)
                .build();
    }
}