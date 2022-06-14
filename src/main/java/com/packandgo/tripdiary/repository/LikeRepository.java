package com.packandgo.tripdiary.repository;

import com.packandgo.tripdiary.model.Like;
import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Boolean existsByTripIdAndUserId(Long TripId, Long UserId);
    Like findByTripIdAndUserId(Long TripId, Long UserId);
}
