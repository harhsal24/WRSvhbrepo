package com.hb.WRSvhb.config.authdtos.authentication;


import com.hb.WRSvhb.config.authdtos.*;
import com.hb.WRSvhb.config.authdtos.exceptions.AppException;
import com.hb.WRSvhb.config.authdtos.security.UserAuthenticationProvider;


import com.hb.WRSvhb.config.authdtos.user.UserService;
import com.hb.WRSvhb.enums.Role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;


    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Validated CredentialsDto credentialsDto) {
        logger.info("Received credentials: Username={}, Password={}", credentialsDto.getLogin(), credentialsDto.getPassword());



        try {
            UserDto userDto = userService.login(credentialsDto);

            if (!userDto.isActive()) {
                logger.info("is account active {}",userDto.isActive());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            List<Role> roles = Collections.singletonList(userDto.getRole());

            userDto.setAccessToken(userAuthenticationProvider.createToken(userDto.getLogin(), roles));
            userDto.setRefreshToken(userAuthenticationProvider.createRefreshToken(userDto.getLogin(), roles));

            logger.info("JWT token created for user: {}", userDto.getLogin());

            return ResponseEntity.ok(userDto);
        } catch (AppException ex) {
            logger.warn("Authentication failed: {}", ex.getMessage());
            throw ex;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {

        UserDto createdUser = userService.register(user);


        if (createdUser != null) {
            List<Role> roles = Collections.singletonList(user.getRole());

            createdUser.setAccessToken(userAuthenticationProvider.createToken(user.getLogin(), roles));
            createdUser.setRefreshToken(userAuthenticationProvider.createRefreshToken(user.getLogin(), roles));

            return ResponseEntity.created(URI.create("/users/" + createdUser.getEmpId())).body(createdUser);
        }
        else {
            // Handle the case where user registration failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String expiredAccessToken = refreshTokenRequest.getAccessToken();
        logger.info("{} expired token "+ expiredAccessToken);

        String refreshToken = refreshTokenRequest.getRefreshToken();
        logger.info("{} refresh token "+ refreshToken);

        String newAccessToken = userAuthenticationProvider.refreshAccessToken(expiredAccessToken, refreshToken);

        if (newAccessToken != null) {
            AccessTokenResponse response = new AccessTokenResponse(newAccessToken);
            return ResponseEntity.ok(response);
        } else {
            // Return an error response if the refresh token is invalid or expired
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }



}
