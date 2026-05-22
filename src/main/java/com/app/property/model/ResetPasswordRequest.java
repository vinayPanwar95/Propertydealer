package com.app.property.model;

public record ResetPasswordRequest(String oldPassword, String newPassword) {
}