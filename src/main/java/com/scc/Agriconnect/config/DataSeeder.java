package com.scc.Agriconnect.config;

import com.scc.Agriconnect.entity.Role;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.repository.RoleRepository;
import com.scc.Agriconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = seedRole("SYSTEM_ADMIN", "Full platform access");
        seedRole("PRESIDENT", "Full cooperative access");
        seedRole("ACCOUNTANT", "Financial sections only");
        seedRole("STAFF", "General staff access");
        seedSystemAdmin(adminRole);
    }

    private Role seedRole(String name, String permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(Role.builder().name(name).permissions(permissions).build()));
    }

    private void seedSystemAdmin(Role adminRole) {
        String email = "admin@agriconnect.rw";
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(User.builder()
                    .fullName("System Administrator")
                    .phoneNumber("0788112233")
                    .email(email)
                    .passwordHash(passwordEncoder.encode("Mbabazi@12"))
                    .role(adminRole)
                    .build());
        }
    }
}