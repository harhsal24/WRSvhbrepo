package com.hb.WRSvhb.config.authentication;

import com.hb.WRSvhb.config.security.myJwt.JwtConfig;
import com.hb.WRSvhb.config.security.myJwt.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtConfig jwtConfig;
    private final JwtUtil jwtUtil;

    public TokenService(JwtConfig jwtConfig, JwtUtil jwtUtil) {
        this.jwtConfig = jwtConfig;
        this.jwtUtil = jwtUtil;
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        return jwtUtil.generateToken(username);
    }

    public String getTokenHeader() {
        return jwtConfig.getHeader();
    }

    public String getTokenPrefix() {
        return jwtConfig.getTokenPrefix();
    }
}
