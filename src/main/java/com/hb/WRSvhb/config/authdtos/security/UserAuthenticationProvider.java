package com.hb.WRSvhb.config.authdtos.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hb.WRSvhb.config.authdtos.UserDto;

import com.hb.WRSvhb.config.authdtos.exceptions.EmployeeNotFoundException;
import com.hb.WRSvhb.config.authdtos.exceptions.InvalidRefreshTokenException;
import com.hb.WRSvhb.config.authdtos.user.UserService;
import com.hb.WRSvhb.enums.Role;
import com.hb.WRSvhb.model.Employee;
import com.hb.WRSvhb.model.RefreshToken;
import com.hb.WRSvhb.repository.EmployeeRepository;
import com.hb.WRSvhb.repository.RefreshTokenRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;


    @Value("${security.jwt.token.access-token-validity:3600000}") // 1 hour in milliseconds
    private long accessTokenValidity;

    @Value("${security.jwt.token.refresh-token-validity:604800000}") // 7 days in milliseconds
    private long refreshTokenValidity;

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private  final EmployeeRepository employeeRepository;

    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationProvider.class);


    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login , List<Role> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity); // 1 hour

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        List<String> roleNames = roles.stream().map(Role::name).collect(Collectors.toList()); // Convert roles to role names
        String token= JWT.create()
                .withSubject(login)
                .withClaim("roles", roleNames)  // Include role names in the token
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);


        log.info("Created token: {}", token);
        log.info("Token validity: {}", now.getTime());
        log.info("Token expiration: {}", validity);

        return token;

    }
    public String createRefreshToken(String login,List<Role> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity); // 7 days


        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        List<String> roleNames = roles.stream().map(Role::name).collect(Collectors.toList());

        String refreshToken= JWT.create()
                .withSubject(login)
                .withClaim("roles", roleNames)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);


        Optional<Employee> employeeOptional = employeeRepository.findByEmail(login);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            // Save the refresh token to the database
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setExpirationDate(LocalDateTime.ofInstant(validity.toInstant(), ZoneId.systemDefault()));
                    // Associate the refresh token with the employee
            newRefreshToken.setEmployee(employee);

            if (employee.getRefreshToken()!=null) {
                // Replace the existing refresh token with the new one
                RefreshToken existingRefreshToken = employee.getRefreshToken();
                existingRefreshToken.setToken(newRefreshToken.getToken());
                existingRefreshToken.setExpirationDate(newRefreshToken.getExpirationDate());

                refreshTokenRepository.save(existingRefreshToken);
            } else {
                refreshTokenRepository.save(newRefreshToken);
            }

            return refreshToken;
        } else {
            // Handle the case where the employee is not found
            throw new EmployeeNotFoundException("Employee with email " + login + " not found.");
        }
    }



    public Authentication validateToken(String token) {


        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        log.info("executed");
        DecodedJWT decoded = verifier.verify(token);

        String login = decoded.getSubject();
        List<String> roleNames = decoded.getClaim("roles").asList(String.class);  // Retrieve role names from the token



        UserDto user = userService.findByLogin(login);

        log.info("Token validation successful for user: {}", user.getLogin());
        log.info("User roles: {}", roleNames);

        Collection<GrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new User(user.getLogin(), "", authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }



    public String refreshAccessToken(String expiredAccessToken, String refreshToken) {
        // Extract username from the expired access token
        String username = extractUsernameFromToken(expiredAccessToken);
        log.info("Refreshing access token for user: {}", username);

        // Validate the refresh token and fetch the associated employee
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(refreshToken);

        if (storedRefreshToken == null || !storedRefreshToken.isValid() || !storedRefreshToken.getEmployee().getEmail().equals(username)) {
            log.warn("Invalid refresh token for user: {}", username);
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        // Generate a new access token
        Employee employee = storedRefreshToken.getEmployee();
        List<Role> roles = Collections.singletonList(employee.getRole());
        String newAccessToken = createToken(employee.getEmail(), roles);
        log.info("Generated new access token for user: {}", username);

        // Generate a new refresh token
        String newRefreshToken = createRefreshToken(employee.getEmail(), roles);
        log.info("Generated new refresh token for user: {}", username);

        return newAccessToken;
    }




    public boolean isTokenExpired(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expiration = decodedJWT.getExpiresAt();
        return expiration != null && expiration.before(new Date());
    }

    public String extractUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    public List<String> extractRolesFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("roles").asList(String.class);
    }

}
