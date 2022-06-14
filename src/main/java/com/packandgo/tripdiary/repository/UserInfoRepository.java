package com.packandgo.tripdiary.repository;

import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    @Query(value = "SELECT u FROM UserInfo u WHERE u.user.id = ?1")
    Optional<UserInfo> findByUserId(Long id);

}
