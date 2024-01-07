package com.example.demo.user.service;

import com.example.demo.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {
    private final MailSender mailSender;


    public void send(final String email, final String certificationCode, final Long userId) {
        final String certificationUrl = generateUrl(userId, certificationCode);
        String title = "Please certify your email address";
        String content = "Please click the following link to certify your email address: " + certificationUrl;
        mailSender.send(email, title, content);
    }

    private String generateUrl(final Long userId, final String certificationCode) {
        return "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationCode;
    }
}
