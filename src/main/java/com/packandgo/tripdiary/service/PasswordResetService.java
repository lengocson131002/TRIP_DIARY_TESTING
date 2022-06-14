package com.packandgo.tripdiary.service;

import com.packandgo.tripdiary.model.User;

public interface PasswordResetService {
    public boolean validatePasswordResetToken(String token);
    public void invalidateToken(String token);
    public User findUserFromToken(String token);

}
