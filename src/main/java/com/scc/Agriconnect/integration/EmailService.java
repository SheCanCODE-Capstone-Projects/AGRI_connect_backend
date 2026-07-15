package com.scc.Agriconnect.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendStaffInvitation(String toEmail, String cooperativeName, String roleName, String token) {
        String acceptLink = frontendUrl + "/accept-invitation?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("You've been invited to join " + cooperativeName + " on AgriConnect");
        message.setText("""
                Hello,

                You have been invited to join %s on AgriConnect as %s.

                Accept your invitation here:
                %s

                — The AgriConnect Team
                """.formatted(cooperativeName, roleName, acceptLink));

        mailSender.send(message);
    }
}