package com.hb.WRSvhb.config.security.myJwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtSecurityConfigurerAdapter extends AbstractHttpConfigurer<JwtSecurityConfigurerAdapter, HttpSecurity> {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public JwtSecurityConfigurerAdapter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
