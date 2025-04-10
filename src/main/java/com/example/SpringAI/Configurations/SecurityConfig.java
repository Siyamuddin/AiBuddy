package com.example.SpringAI.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {



    private OauthService handler;
    public SecurityConfig(OauthService handler) {
        this.handler = handler;
    }
    private static final String[] PUBLIC_URLS= {
            "/auth/login/**",
            "/auth/register/**",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"

    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, Jackson2ObjectMapperBuilderCustomizer customizer) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login/**","/auth/register/**","/v3/api-docs/**","/",
                                "/v2/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/login") // Match the root URL correctly
                        .permitAll() // Allow access to the root URL and public URLs without authentication
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth2login -> oauth2login.successHandler(handler));

        return http.build();
    }


}
