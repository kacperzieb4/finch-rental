package com.finchrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Wyłączenie CSRF dla API bezstanowego oraz dla konsoli H2
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
            // Wyłączenie blokowania ramek (frame options) dla konsoli H2
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            // Bezstanowa sesja pod późniejsze JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Zezwolenie na wszystkie zapytania na tym etapie (w tym konsolę H2)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
