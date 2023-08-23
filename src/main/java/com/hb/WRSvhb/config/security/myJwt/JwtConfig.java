package com.hb.WRSvhb.config.security.myJwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}") // You need to define this property in your application.properties or application.yml
    private String secret;

    @Value("${jwt.expiration}") // You need to define this property in your application.properties or application.yml
    private int expiration;

    @Value("${jwt.header}") // You need to define this property in your application.properties or application.yml
    private String header;

    @Value("${jwt.tokenPrefix}") // You need to define this property in your application.properties or application.yml
    private String tokenPrefix;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000); // Expiration time in milliseconds

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("iat", now);
        claims.put("exp", expiryDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getHeader() {
        return header;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }
}
