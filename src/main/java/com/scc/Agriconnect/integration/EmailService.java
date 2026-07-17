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
    public void sendRegistrationConfirmation(String toEmail, String fullName, String cooperativeName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Thank you for registering" + cooperativeName + "on AgriConnect");
        message.setText("""
            Hello %s,

            Thank you for registering %s on AgriConnect.

            Your cooperative is currently under review by our team. We will notify
            you by email as soon as it has been approved, and you'll be able to
            log in and start managing your cooperative.

            — The AgriConnect Team
            """.formatted(fullName, cooperativeName));

        mailSender.send(message);
    }
    public void sendApprovalNotification(String toEmail, String fullName, String cooperativeName){
        String loginLink = frontendUrl + "/login";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your cooperative has been approved on Agriconnect");
        message.setText("""
                       Hello %s,
                
                       Thank you for registering %s on AgriConnect.
                
                       Your cooperative is currently under review by our team. We will notify
                       you by email as soon as it has been approved, and you'll be able to
                       log in and start managing your cooperative.
                
                       — The AgriConnect Team     
                """.formatted(fullName, cooperativeName));
        mailSender.send(message);
    }
    public void sendRejectionNotification(String toEmail, String fullName, String cooperativeName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Update on your AgriConnect cooperative registration");
        message.setText("""
            Hello %s,

            Thank you for your interest in AgriConnect.

            After review, we were unable to approve %s at this time.

            If you believe this was a mistake or would like more information,
            please contact our support team.

            — The AgriConnect Team
            """.formatted(fullName, cooperativeName));

        mailSender.send(message);
    }
}