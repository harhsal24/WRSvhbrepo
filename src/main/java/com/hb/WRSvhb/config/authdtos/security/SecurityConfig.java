package com.hb.WRSvhb.config.authdtos.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .exceptionHandling(ex->ex.authenticationEntryPoint(userAuthenticationEntryPoint))
//                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
//                .csrf(csrf->csrf.disable())
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
//                        .anyRequest().authenticated());
//        return http.build();
//    }


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           return http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                   .csrf(csrf->csrf.disable())
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(userAuthenticationEntryPoint))
                    .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                    .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/").permitAll();
                        auth.requestMatchers(HttpMethod.POST,"/login").permitAll();
                        auth.requestMatchers(HttpMethod.POST,"/register").permitAll();
                        auth.requestMatchers("/error").permitAll();
                        auth.requestMatchers("/admin/**").hasRole("SUPER_ADMIN");
                        auth.requestMatchers("/favicon.ico").permitAll();
                        auth.anyRequest().permitAll();
                    })

                    .build();

        }

         @Bean
     public CorsConfigurationSource corsConfigurationSource() {
         CorsConfiguration configuration = new CorsConfiguration();
         configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
         configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
         configuration.setAllowedHeaders(Arrays.asList("*"));
         configuration.setExposedHeaders(Arrays.asList("Authorization","Content-Type"));
         configuration.setAllowCredentials(true);

         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration("/**", configuration);
         return source;
     }


    }





