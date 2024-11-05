package com.example.SpringAI.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MailSenderServices {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String mailFrom;
    public void sendEmail(String to,String subject,String body)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        Date date=new Date();
        message.setSentDate(date);
        message.setText(body);
        javaMailSender.send(message);
    }
}

