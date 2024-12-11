package com.example.SpringAI.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private OauthService handler;
    public static final String[] PUBLIC_URLS={
            "/auth/login/**",
            "/auth/register/**",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
//            "/api/v1/**",
//            "/api/v1/slide/**"
    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/view/upload/slide",  // Allow the upload page
                                "/",
                                "/auth/login/**",
                                "/auth/register/**"
                        )
                        .permitAll() // Permit these endpoints without authentication
                        .anyRequest()
                        .authenticated() // Require authentication for other endpoints
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(handler) // Define a custom success handler
                );

        return http.build();
    }



//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http, Jackson2ObjectMapperBuilderCustomizer customizer) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth-> auth
//                        .requestMatchers(PUBLIC_URLS,'http://localhost:8080')
//                        .permitAll()
//                        .anyRequest()
//                        .authenticated()
//                )
//                .oauth2Login(Customizer.withDefaults())
//                .oauth2Login(oauth2login->{
//                    oauth2login.successHandler(handler);
//                });
//
//
//          return http.build();
//    }
}
