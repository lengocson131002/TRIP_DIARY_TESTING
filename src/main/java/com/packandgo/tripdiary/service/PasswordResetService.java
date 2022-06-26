package com.packandgo.tripdiary.service;

import com.packandgo.tripdiary.model.Notification;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.payload.response.PagingResponse;

public interface PasswordResetService {
    public boolean validatePasswordResetToken(String token);
    public void invalidateToken(String token);
    public User findUserFromToken(String token);
    
}
