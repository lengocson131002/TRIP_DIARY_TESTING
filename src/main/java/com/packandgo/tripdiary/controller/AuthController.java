package com.packandgo.tripdiary.controller;

import com.packandgo.tripdiary.auth.UserDetailsImpl;
import com.packandgo.tripdiary.constants.BaseUrl;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.model.mail.MailContent;
import com.packandgo.tripdiary.model.mail.ResetPasswordMailContent;
import com.packandgo.tripdiary.model.mail.VerifyEmailMailContent;
import com.packandgo.tripdiary.payload.request.auth.LoginRequest;
import com.packandgo.tripdiary.payload.request.auth.PasswordResetRequest;
import com.packandgo.tripdiary.payload.request.auth.NewPasswordRequest;
import com.packandgo.tripdiary.payload.request.auth.RegisterRequest;
import com.packandgo.tripdiary.payload.response.JwtResponse;
import com.packandgo.tripdiary.payload.response.MessageResponse;
import com.packandgo.tripdiary.service.EmailSenderService;
import com.packandgo.tripdiary.service.PasswordResetService;
import com.packandgo.tripdiary.service.UserService;
import com.packandgo.tripdiary.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.net.URI;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final EmailSenderService emailSenderService;
    private final PasswordResetService passwordResetService;

    @Value("${tripdiary.baseurl.frontend}")
    private String frontendUrl;

    @Value("${tripdiary.baseurl.backend}")
    private String backendUrl;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtUtils jwtUtils,
                          EmailSenderService emailSenderService,
                          PasswordResetService passwordResetService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.emailSenderService = emailSenderService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword())
            );
        } catch (Exception exception) {
            throw new IllegalArgumentException("Email(username) or password is not correct");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findUserByUsername(userDetails.getUsername());

        if (!user.isEnabled()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Account hasn't been verified"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        JwtResponse body = new JwtResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getEmail());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request) throws Exception {

        userService.register(registerRequest, getSiteURL(request));

        return ResponseEntity
                .ok()
                .body(new MessageResponse("User register successfully. Check email to verify"));
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<?> resetPasswordRequest(@RequestBody PasswordResetRequest resetPasswordRequest,
                                                  HttpServletRequest request) {
        //check if it is a existed email
        String email = resetPasswordRequest.getEmail();
        boolean existedEmail = userService.existsByEmail(email);

        if (!existedEmail) {
            throw new IllegalArgumentException("Email does not exist");
        }

        User user = userService.findUserByEmail(email);
        String token = userService.createPasswordResetTokenForUser(user);

        //sendEmail
        //create reset password email;
        MailContent mailContent = new ResetPasswordMailContent(user.getEmail(), token, frontendUrl);
        emailSenderService.sendEmail(mailContent);

        return ResponseEntity.ok(new MessageResponse("Request to reset password sent. Check your email for detail"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody NewPasswordRequest newPasswordRequest) {

        userService.resetPassword(newPasswordRequest);
        //validate token;
        return ResponseEntity.ok(new MessageResponse("Change password successfully"));

    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@PathParam("token") String token) {
        boolean isValidToken = userService.verify(token);
        if (isValidToken) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/login")).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl + "/invalid-verify-code")).build();
        }
    }
}