package com.app.property.controller;

import com.app.property.model.AdminSetupRequest;
import com.app.property.model.ResetPasswordRequest;
import com.app.property.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

    private final AdminUserService adminUserService;

    @GetMapping("/auth/setup")
    public String setupPage(@RequestParam(required = false) String success, Model model) {
        if (success != null) {
            model.addAttribute("success", "Admin account created successfully. You can now log in.");
        }
        return "auth/setup";
    }

    @PostMapping("/auth/setup")
    public String doSetup(@RequestParam String username,
                          @RequestParam String password,
                          Model model) {
        try {
            adminUserService.setupAdmin(new AdminSetupRequest(username, password));
            return "redirect:/auth/setup?success";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/setup";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Admin account already exists. Use reset-password to change credentials.");
            return "auth/setup";
        }
    }

    @GetMapping("/admin/reset-password")
    public String resetPasswordPage() {
        return "admin/reset-password";
    }

    @PostMapping("/admin/reset-password")
    public String doResetPassword(@RequestParam String oldPassword,
                                  @RequestParam String newPassword,
                                  Principal principal,
                                  Model model) {
        try {
            adminUserService.resetPassword(principal.getName(), new ResetPasswordRequest(oldPassword, newPassword));
            model.addAttribute("success", "Password changed successfully.");
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Current password is incorrect.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/reset-password";
    }
}
