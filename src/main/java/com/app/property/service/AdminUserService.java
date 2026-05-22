package com.app.property.service;

import com.app.property.entity.AdminUser;
import com.app.property.model.AdminSetupRequest;
import com.app.property.model.ResetPasswordRequest;
import com.app.property.repository.AdminUserRepository;
import com.app.property.util.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates the admin account. Fails if one already exists (one-time setup only).
     */
    public void setupAdmin(AdminSetupRequest request) {
        if (adminUserRepository.count() > 0) {
            throw new IllegalStateException("Admin account already exists. Use reset-password to change credentials.");
        }

        var admin = AdminUser.builder()
                .id(UuidGenerator.generate())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();

        adminUserRepository.save(admin);
    }

    /**
     * Resets the password for the given username after verifying the old password.
     */
    public void resetPassword(String username, ResetPasswordRequest request) {
        var admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (!passwordEncoder.matches(request.oldPassword(), admin.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        admin.setPassword(passwordEncoder.encode(request.newPassword()));
        adminUserRepository.save(admin);
    }
}