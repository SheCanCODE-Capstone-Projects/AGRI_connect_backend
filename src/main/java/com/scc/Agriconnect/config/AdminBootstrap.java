package com.scc.Agriconnect.config;

import com.scc.Agriconnect.entity.RoleType;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = User.builder()
                .fullName("System Administrator")
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .role(RoleType.SYSTEM_ADMIN)
                .status(User.UserStatus.ACTIVE)
                .cooperative(null)
                .build();

        userRepository.save(admin);
    }
}