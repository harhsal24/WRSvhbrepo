package com.hb.WRSvhb.config.authdtos.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hb.WRSvhb.config.authdtos.UserDto;

import com.hb.WRSvhb.config.authdtos.user.UserService;
import com.hb.WRSvhb.enums.Role;
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

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey="my-secret-key";

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationProvider.class);


    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login , List<Role> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        List<String> roleNames = roles.stream().map(Role::name).collect(Collectors.toList()); // Convert roles to role names
        return JWT.create()
                .withSubject(login)
                .withClaim("roles", roleNames)  // Include role names in the token
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public Authentication validateToken(String token) {


        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

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



    public String refreshExpiredToken(String expiredToken) {
        DecodedJWT decoded = JWT.decode(expiredToken);
        String login = decoded.getSubject();

        // Calculate the remaining time until token expiration
        Date expirationTime = decoded.getExpiresAt();
        Date currentTime = new Date();
        long remainingMillis = expirationTime.getTime() - currentTime.getTime();

        // Define a threshold (e.g., 5 minutes)
        long thresholdMillis = 5 * 60 * 1000; // 5 minutes in milliseconds

        // Check if the remaining time is within the threshold
        if (remainingMillis <= thresholdMillis) {
            // Token is about to expire, generate a new token with extended validity
            Role roles = userService.findByLogin(login).getRole();
            return createToken(login,List.of(roles) );
        } else {
            // Token is not about to expire, return the expired token as is
            return expiredToken;
        }
    }

}
