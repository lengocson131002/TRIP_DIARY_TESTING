package com.packandgo.tripdiary.service.impl;

import com.packandgo.tripdiary.model.PasswordResetToken;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.repository.PasswordResetRepository;
import com.packandgo.tripdiary.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;

    @Autowired
    public PasswordResetServiceImpl(PasswordResetRepository passwordResetRepository) {
        this.passwordResetRepository = passwordResetRepository;
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken pToken  = passwordResetRepository.findByToken(token);

        if(pToken == null) {
            throw new IllegalArgumentException("Invalid reset password token");
        }

        if(isTokenExpired(pToken)) {
            invalidateToken(token);
        }
        return isTokenFound(pToken) && !isTokenExpired(pToken);
    }

    @Override
    @Transactional
    public void invalidateToken(String token) {
        PasswordResetToken pToken  = passwordResetRepository.findByToken(token);
        passwordResetRepository.delete(pToken);
    }

    @Override
    public User findUserFromToken(String token) {
        PasswordResetToken pToken = passwordResetRepository.findByToken(token);

        if(pToken == null) {
            return null;
        }

        return pToken.getUser();
    }


    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }
    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }


}
