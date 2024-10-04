package com.example.SpringAI.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MailSenderServices {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to,String subject,String body)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("siyamuddin177@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        Date date=new Date();
        message.setSentDate(date);
        message.setText(body);
        javaMailSender.send(message);
    }
}

