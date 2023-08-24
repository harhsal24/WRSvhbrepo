package com.hb.WRSvhb.config.authdtos.authentication;


import com.hb.WRSvhb.config.authdtos.*;
import com.hb.WRSvhb.config.authdtos.exceptions.AppException;
import com.hb.WRSvhb.config.authdtos.security.UserAuthenticationProvider;


import com.hb.WRSvhb.config.authdtos.user.UserService;
import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
            logger.info("Authentication successful for user: {}", userDto.getLogin());

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

        List<Role> roles = Collections.singletonList(user.getRole());

        createdUser.setAccessToken(userAuthenticationProvider.createToken(user.getLogin(), roles));
        createdUser.setRefreshToken(userAuthenticationProvider.createRefreshToken(user.getLogin(), roles));

        return ResponseEntity.created(URI.create("/users/" + createdUser.getEmpId())).body(createdUser);
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




}
