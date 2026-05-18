package com.app.property.controller;

import com.app.property.model.Contact;
import com.app.property.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling contact form submissions
 */
@Controller
@RequestMapping("/contact")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactController {

    private final  EmailService emailService;

    @GetMapping("/form")
    public String contactForm() {

        System.err.println(":loading contact form");
        return "public/contact";
    }


    @PostMapping
    public ResponseEntity<?> submitContact(@RequestBody Contact contact) {
        try {
            // Validate input
            if (contact.getName() == null || contact.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Name is required"));
            }
            if (contact.getEmail() == null || contact.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is required"));
            }
            if (contact.getPhone() == null || contact.getPhone().length() != 10) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Valid 10-digit phone number is required"));
            }
            if (contact.getEnquiry() == null || contact.getEnquiry().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Enquiry message is required"));
            }

            // Send email to admin
            emailService.sendContactEnquiry(contact);

            // Send confirmation email to user
            emailService.sendConfirmationEmail(contact);

            return ResponseEntity.ok(new ApiResponse(true, "Thank you for your enquiry! We will contact you soon."));

        } catch (Exception e) {
            System.err.println("Error processing contact form: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error sending enquiry. Please try again later."));
        }
    }

    /**
     * Simple response wrapper
     */
    public static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}

