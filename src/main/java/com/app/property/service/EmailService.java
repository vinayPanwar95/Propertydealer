package com.app.property.service;

import com.app.property.model.Contact;
import lombok.RequiredArgsConstructor;
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
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.recipient}")
    private String recipientEmail;

    @Value("${app.mail.from}")
    private String fromEmail;

//    @Value("${app.mail.username}")
//    private String userName;
//
//    @Value("${app.mail.appcode}")
//    private String appCode;

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

            System.err.println("Preparing to send email to: " + recipientEmail);
            System.err.println("Email content:\n" + body);
            message.setText(body);
            System.err.println("Email message prepared successfully, sending now...");

            mailSender.send(message);

            System.out.println("Email sent successfully to: " + recipientEmail);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
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

            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(contact.getName()).append(",\n\n");
            body.append("Thank you for your enquiry about our ").append(contact.getPropertyType()).append(" property.\n\n");
            body.append("We have received your message and will get back to you within 24 hours.\n\n");
            body.append("Your Enquiry Details:\n");
            body.append("Phone: ").append(contact.getPhone()).append("\n");
            body.append("Email: ").append(contact.getEmail()).append("\n\n");
            body.append("Best regards,\n");
            body.append("Shree Nath Ji Associates Team\n\n");
            body.append("Contact: +91-99996-49161\n");
            body.append("Email: shreeenathjiassociates@gmail.com");

            message.setText(body.toString());

            mailSender.send(message);

            System.out.println("Confirmation email sent to: " + contact.getEmail());
        } catch (Exception e) {
            System.err.println("Error sending confirmation email: " + e.getMessage());
            // Don't throw exception for confirmation email failure
        }
    }
}

