package com.app.property.service;

import com.app.property.model.Contact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialRef;

/**
 * Service for handling email operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.recipient}")
    private String recipientEmail;

    @Value("${app.mail.from}")
    private String fromEmail;
    /**
     * Send contact form enquiry email
     */
    @Async
    public void sendContactEnquiry(Contact contact) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(recipientEmail);
            message.setReplyTo(contact.getEmail());
            message.setSubject("New Property Enquiry from " + contact.getName());

            String body = "New enquiry received from the contact form:\n\n" +
                    "Name: " + contact.getName() + "\n" +
                    "Phone: " + contact.getPhone() + "\n" +
                    "Email: " + contact.getEmail() + "\n" +
                    "Property Type: " + contact.getPropertyType() + "\n" +
                    "\n--- Enquiry Message ---\n" +
                    contact.getEnquiry() + "\n\n" +
                    "--- Automated Message ---\n" +
                    "Please reply to: " + contact.getEmail() + "\n" +
                    "Or call: " + contact.getPhone();

            message.setText(body);
            log.info("Prepared email message for contact enquiry from: {}", contact.getEmail());

            mailSender.send(message);
            log.info("Sent email for contact enquiry from: {}", recipientEmail);
        } catch (Exception e) {
            log.error("Error sending email for contact enquiry from {}: {}", contact.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
    /**
     * Send confirmation email to user
     */
    public void sendConfirmationEmail(Contact contact) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(contact.getEmail());
            message.setSubject("Thank you for your enquiry - Shree Nath Ji Associates");

            String body = "Dear " + contact.getName() + ",\n\n" +
                    "Thank you for your enquiry about our " + contact.getPropertyType() + " property.\n\n" +
                    "We have received your message and will get back to you within 24 hours.\n\n" +
                    "Your Enquiry Details:\n" +
                    "Phone: " + contact.getPhone() + "\n" +
                    "Email: " + contact.getEmail() + "\n\n" +
                    "Best regards,\n" +
                    "Shree Nath Ji Associates Team\n\n" +
                    "Contact: +91-99996-49161\n" +
                    "Email: shreeenathjiassociates@gmail.com";

            message.setText(body);
            mailSender.send(message);
            log.info("Sent confirmation email to: {}", contact.getEmail());
        } catch (Exception e) {
            log.error("Error sending confirmation email to {}: {}", contact.getEmail(), e.getMessage());
        }
    }
}

