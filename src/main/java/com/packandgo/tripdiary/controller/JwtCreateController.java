package com.packandgo.tripdiary.controller;

import com.packandgo.tripdiary.auth.oauth2.GoogleOAuth2UserInfo;
import com.packandgo.tripdiary.auth.oauth2.OAuth2UserInfo;
import com.packandgo.tripdiary.enums.UserStatus;
import com.packandgo.tripdiary.model.Role;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.model.UserInfo;
import com.packandgo.tripdiary.payload.response.JwtResponse;
import com.packandgo.tripdiary.repository.RoleRepository;
import com.packandgo.tripdiary.repository.UserInfoRepository;
import com.packandgo.tripdiary.repository.UserRepository;
import com.packandgo.tripdiary.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class JwtCreateController {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public JwtCreateController(JwtUtils jwtUtils,
                               UserRepository userRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder,
                               UserInfoRepository userInfoRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
    }

    @PostMapping("/oauth2/jwt/google")
    public ResponseEntity<?> generateJwtToken(@RequestBody Map<String, Object> data) {
        System.out.println("JWT Creating...");

        OAuth2UserInfo userInfo = new GoogleOAuth2UserInfo((Map<String, Object>) data.get("profileObj"));

        User user =
                userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> null);

        if (user == null) {
            //create user
            user = new User();

            String usernameFromEmail = userInfo.getEmail().substring(0, userInfo.getEmail().indexOf("@"));
            user.setUsername(usernameFromEmail);
            user.setEmail(userInfo.getEmail());
            user.setStatus(UserStatus.ACTIVE);

            Role role = roleRepository.findByName("USER").orElseGet(() -> new Role("USER"));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);

            UserInfo info = new UserInfo();
            info.setProfileImageUrl(userInfo.getProfileImageUrl());
            info.setUser(user);

            userInfoRepository.save(info);

        } else if (!user.isEnabled()) {
            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);

        String jwtToken = jwtUtils.generateJwtTokenFromUsername(user.getUsername());

        JwtResponse response = new JwtResponse(
                jwtToken,
                user.getUsername(),
                user.getEmail());

        return ResponseEntity.ok(response);
    }
}
