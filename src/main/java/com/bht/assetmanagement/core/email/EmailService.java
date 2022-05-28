package com.bht.assetmanagement.core.email;

import com.bht.assetmanagement.shared.exception.EmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendActivationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("foo@bar.com");
        message.setTo(email);
        message.setSubject("Aktivierungslink");
        String text = "Please click on the below url to activate your account: http://localhost:8080/auth/activate/account/" + token;
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(String.format("Email could not be sent to %s", email));
        }
    }

    @Async
    public void sendAssetStatusMail(String email, Boolean isEnabled) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("foo@bar.com");
        message.setTo(email);
        message.setSubject("Asset Status");
        String textDisabled = "Der Status Ihrer Asset Anfrage hat sich geändert. Leider musste Ihre Anfrage abgelehnt werden";
        String textEnabled = "Der Status Ihrer Asset Anfrage hat sich geändert. Ihre Anfrage wurde angenommen.";

        if (isEnabled) {
            message.setText(textEnabled);
        } else {
            message.setText(textDisabled);
        }
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(String.format("Email could not be sent to %s", email));
        }
    }
}
