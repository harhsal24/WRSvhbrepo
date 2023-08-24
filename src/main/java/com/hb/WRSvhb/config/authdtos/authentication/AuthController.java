package com.hb.WRSvhb.config.authdtos.authentication;


import com.hb.WRSvhb.config.authdtos.CredentialsDto;
import com.hb.WRSvhb.config.authdtos.SignUpDto;
import com.hb.WRSvhb.config.authdtos.UserDto;
import com.hb.WRSvhb.config.authdtos.exceptions.AppException;
import com.hb.WRSvhb.config.authdtos.security.UserAuthenticationProvider;


import com.hb.WRSvhb.config.authdtos.user.UserService;
import com.hb.WRSvhb.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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

            userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin(),List.of(userDto.getRole())));
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
        createdUser.setToken(userAuthenticationProvider.createToken(user.getLogin(), List.of(user.getRole())));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getEmpId())).body(createdUser);
    }

}
