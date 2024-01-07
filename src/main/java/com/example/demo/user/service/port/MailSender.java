package com.example.demo.user.service.port;

import org.springframework.stereotype.Component;

@Component
public interface MailSender {
    void send(String email, String title, String content);
}
