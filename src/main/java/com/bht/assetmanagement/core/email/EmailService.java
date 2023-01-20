package com.bht.assetmanagement.core.email;

import com.bht.assetmanagement.shared.exception.EmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${app.mail.enabled}")
    private boolean active;
    private final JavaMailSender mailSender;

    public void sendMessage(String to, String subject, String body) {
        if(active){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("info@myAssets.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            try {
                mailSender.send(message);
            } catch (MailException e) {
                throw new EmailException(String.format("Email could not be sent to %s", to));
            }
        }
    }

    public void sendMessage(String from, String to, String subject, String body) {
        if(active) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(String.format("Email could not be sent to %s", to));
        }
    }
    }
}
