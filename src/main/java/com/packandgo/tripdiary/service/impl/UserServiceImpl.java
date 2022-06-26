package com.packandgo.tripdiary.service.impl;

import com.packandgo.tripdiary.enums.Gender;
import com.packandgo.tripdiary.enums.TripStatus;
import com.packandgo.tripdiary.enums.UserStatus;
import com.packandgo.tripdiary.exception.UserNotFoundException;
import com.packandgo.tripdiary.model.*;
import com.packandgo.tripdiary.model.mail.MailContent;
import com.packandgo.tripdiary.model.mail.VerifyEmailMailContent;
import com.packandgo.tripdiary.payload.request.auth.NewPasswordRequest;
import com.packandgo.tripdiary.payload.request.auth.RegisterRequest;
import com.packandgo.tripdiary.payload.request.user.InfoUpdateRequest;
import com.packandgo.tripdiary.payload.response.TripResponse;
import com.packandgo.tripdiary.payload.response.UserResponse;
import com.packandgo.tripdiary.repository.PasswordResetRepository;
import com.packandgo.tripdiary.repository.RoleRepository;
import com.packandgo.tripdiary.repository.UserInfoRepository;
import com.packandgo.tripdiary.repository.UserRepository;
import com.packandgo.tripdiary.service.EmailSenderService;
import com.packandgo.tripdiary.service.PasswordResetService;
import com.packandgo.tripdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final PasswordResetService passwordResetService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserInfoRepository userInfoRepository,
                           RoleRepository roleRepository, PasswordResetRepository passwordResetRepository,
                           PasswordEncoder passwordEncoder,
                           EmailSenderService emailSenderService, PasswordResetService passwordResetService) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.roleRepository = roleRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
        this.passwordResetService = passwordResetService;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email \"" + email + "\" doesn't exist"));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username \"" + username + "\" doesn't exist"));
    }

    @Override
    public User findUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    @Transactional
    public String createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(new Date(new Date().getTime() + PasswordResetToken.EXPIRE_DURATION));

        passwordResetRepository.save(passwordResetToken);

        return token;

    }

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest, String backendUrl) throws Exception {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username has already exist");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email has already exist");
        }

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        Role role = roleRepository.findByName("USER").orElseGet(() -> new Role("USER"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        //create user info
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setGender(Gender.UNDEFINED);

        //create verify email
        MailContent mailContent = new VerifyEmailMailContent(user.getEmail(), user.getVerifyToken(), backendUrl);
        emailSenderService.sendEmail(mailContent);
        userRepository.save(user);
        userInfoRepository.save(userInfo);
    }

    @Override
    @Transactional
    public void changePassword(User user, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password is required");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean verify(String verifyToken) {
        User user = userRepository.findByVerifyToken(verifyToken);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerifyToken(null);
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            return true;
        }

    }

    @Override
    @Transactional
    public void removeUser(String username) {
        boolean isExist = userRepository.existsByUsername(username);
        if (!isExist) {
            throw new UsernameNotFoundException("User with username \"" + username + "\" doesn't exist");
        }

        userRepository.removeUserByUsername(username);
    }

    @Override
    @Transactional
    public void saveUserInfo(UserInfo info) {
        userInfoRepository.save(info);
    }

    @Override
    public void resetPassword(NewPasswordRequest request) {
        String passwordResetToken = request.getToken();

        boolean isValidToken = passwordResetService.validatePasswordResetToken(passwordResetToken);

        if (!isValidToken) {
            throw new IllegalArgumentException("Reset password token is expired");
        }

        User user = passwordResetService.findUserFromToken(passwordResetToken);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with token");
        }

        this.changePassword(user, request.getNewPassword());
        passwordResetService.invalidateToken(passwordResetToken);
    }


    @Override
    @Transactional
    public void updateUserInfo(User user, InfoUpdateRequest infoUpdateRequest) {

        UserInfo userInfo = userInfoRepository.findByUserId(user.getId()).orElseGet(() -> null);

        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setGender(Gender.UNDEFINED);
            userInfo.setUser(user);
        }

        userInfo.setFirstName(infoUpdateRequest.getFirstName());
        userInfo.setLastName(infoUpdateRequest.getLastName());
        userInfo.setPhoneNumber(infoUpdateRequest.getPhoneNumber());
        userInfo.setCity(infoUpdateRequest.getCity());
        userInfo.setCountry(infoUpdateRequest.getCountry());
        userInfo.setProfileImageUrl(infoUpdateRequest.getProfileImageUrl());
        userInfo.setCoverImageUrl(infoUpdateRequest.getCoverImageUrl());

        if (infoUpdateRequest.getGender() == null) {
            userInfo.setGender(Gender.UNDEFINED);
        } else {
            switch (infoUpdateRequest.getGender()) {
                case "male": {
                    userInfo.setGender(Gender.MALE);
                    break;
                }
                case "female": {
                    userInfo.setGender(Gender.FEMALE);
                    break;
                }
                default: {
                    userInfo.setGender(Gender.UNDEFINED);
                    break;
                }
            }
        }

        userInfo.setDateOfBirth(infoUpdateRequest.getDateOfBirth());
        userInfo.setAboutMe(infoUpdateRequest.getAboutMe());

        try {
            userInfoRepository.save(userInfo);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Can't update user info. Try again");
        }


    }

    @Override
    public List<Trip> getTripsForUser(User user, String me) {
        List<Trip> allTrips = userRepository.findsTripByUserId(user.getId());
        List<Trip> trips = new ArrayList<>();
        if(me != null && (("me".equals(me) || "ME".equals(me)))) {
            trips = allTrips;
        } else {
            trips = allTrips.stream().filter(t -> t.getStatus().equals(TripStatus.PUBLIC)).collect(Collectors.toList());
        }

        return trips;
    }

    @Override
    public Page<UserResponse> getUsersAndAllTrips(int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<User> resultUser = userRepository.findUsersAndAllTrips(paging);

        //only get public trips
        Page<UserResponse> resultUserResponse = resultUser.map(user -> {

            List<Trip> trips = user.getTrips()
                    .stream()
                    .filter(t -> TripStatus.PUBLIC.equals(t.getStatus()))
                    .collect(Collectors.toList());
            List<TripResponse> tripResponses = trips
                    .stream()
                    .map(trip -> trip.toResponse())
                    .collect(Collectors.toList());

            UserResponse response = new UserResponse();
            UserInfo info = getInfo(user);
            response.setUsername(user.getUsername());
            response.setAboutMe(info.getAboutMe());
            response.setCountry(info.getCountry());
            response.setCoverImageUrl(info.getCoverImageUrl());
            response.setProfileImageUrl(info.getProfileImageUrl());
            response.setTrips(tripResponses);
            return response;
        });
        return resultUserResponse;
    }

    @Override
    public List<UserResponse> search(String keyword) {
        List<UserResponse> users = new ArrayList<>();
        if(keyword == null || "".equals(keyword)) {
            return users;
        }
        List<User> searchResult = userRepository.search("%" + keyword.toLowerCase()+ "%");
        users = searchResult
                .stream()
                .map(u -> {

                    /**
                     *  private String username;
                     *     private String avatar;
                     *     private String aboutMe;
                     *     private String country;
                     *     private String profileImageUrl;
                     *     private String coverImageUrl;
                     *     private List<Trip> trips;
                     */
                    UserResponse userResponse = new UserResponse();
                    UserInfo info = userInfoRepository.findByUserId(u.getId()).orElse(new UserInfo());
                    userResponse.setUsername(u.getUsername());
                    userResponse.setAboutMe(info.getAboutMe());
                    userResponse.setCountry(info.getCountry());
                    userResponse.setProfileImageUrl(info.getProfileImageUrl());
                    userResponse.setCoverImageUrl(info.getCoverImageUrl());
                    userResponse.setTrips(
                            u.getTrips()
                                    .stream()
                                    .filter(t -> TripStatus.PUBLIC.equals(t.getStatus()))
                                    .map(t -> t.toResponse())
                                    .collect(Collectors.toList()));

                    return userResponse;
                })
                .collect(Collectors.toList());
        return users;
    }

    @Override
    public UserInfo getInfo(User user) {
        return userInfoRepository.findByUserId(user.getId()).orElseGet(() -> null);
    }

}
