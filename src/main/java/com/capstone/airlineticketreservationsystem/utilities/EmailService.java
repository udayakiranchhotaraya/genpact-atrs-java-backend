package com.capstone.airlineticketreservationsystem.utilities;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${app.base.url}")
    private String baseUrl;

    public void sendHtmlVerificationEmail(String toEmail, String userUUID) throws MessagingException {
        String token = jwtTokenUtil.generateVerificationToken(userUUID, toEmail);
        String verificationUrl = baseUrl + "/set-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("genpactcapstoneatrs2025@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Verify Your Account for Airline Ticket Reservation System");

        String htmlContent = """
                <h3>Welcome to the Airline Ticket Reservation System</h3>
                <p>Thank you for registering with us. To complete your account setup, please create your password using the link below.</p>
                <p><a href="%s">Set Your Password</a></p>
                <p>If you did not initiate this registration, please ignore this email.</p>
            """.formatted(verificationUrl);
        helper.setText(htmlContent, true); // 'true' indicates the text is HTML

        mailSender.send(message);
    }
}
