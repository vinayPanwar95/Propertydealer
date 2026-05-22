package com.app.property.controller;

import com.app.property.model.AdminSetupRequest;
import com.app.property.model.ResetPasswordRequest;
import com.app.property.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserService adminUserService;

    /**
     * One-time admin setup endpoint. Accessible without authentication.
     * Fails if an admin account already exists.
     *
     * POST /api/auth/setup
     * Body: { "username": "admin", "password": "secret123" }
     */
    @PostMapping("/api/auth/setup")
    public ResponseEntity<String> setup(@RequestBody AdminSetupRequest request) {
        try {
            adminUserService.setupAdmin(request);
            return ResponseEntity.ok("Admin account created successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Reset password for the currently authenticated admin.
     * Requires ADMIN role. Old password must be provided for verification.
     *
     * POST /admin/api/reset-password
     * Body: { "oldPassword": "secret123", "newPassword": "newSecret456" }
     */
    @PostMapping("/admin/api/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request,
                                                Principal principal) {
        try {
            adminUserService.resetPassword(principal.getName(), request);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}