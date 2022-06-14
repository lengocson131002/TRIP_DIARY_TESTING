package com.packandgo.tripdiary.repository;

import com.packandgo.tripdiary.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {
    public PasswordResetToken findByToken(String token);
}
