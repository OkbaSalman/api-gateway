package com.okbasalman.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // @Value("${keycloak.auth-server-url}")
    // private String keycloakUrl;

    // @Value("${keycloak.realm}")
    // private String realm;
    
    @Bean
    // JwtDecoder jwtDecoder
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
            //.csrf(csrf -> csrf.disable());

        return http.build();
    }

     //@Bean
    // public JwtDecoder jwtDecoder() {
    //     // Let Spring auto-discover the issuer URI from your Keycloak config
    //     return JwtDecoders.fromIssuerLocation(keycloakUrl + "/realms/" + realm);
    // }
}

