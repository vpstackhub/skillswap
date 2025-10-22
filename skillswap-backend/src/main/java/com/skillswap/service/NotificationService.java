package com.skillswap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void notifyStudent(String email, String message) {
        sendEmail(email, "SkillSwap Booking Update", message);
        System.out.println("📧 Email sent to student: " + email + " | " + message);
    }

    public void notifyTeacher(String email, String message) {
        sendEmail(email, "SkillSwap Booking Notification", message);
        System.out.println("📧 Email sent to teacher: " + email + " | " + message);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send email: " + e.getMessage());
        }
    }
}
