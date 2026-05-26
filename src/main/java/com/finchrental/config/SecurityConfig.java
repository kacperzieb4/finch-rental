package com.finchrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Wyłączenie CSRF dla API bezstanowego oraz dla konsoli H2
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
            // Wyłączenie blokowania ramek (frame options) dla konsoli H2
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            // Bezstanowa sesja pod późniejsze JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Konfiguracja autoryzacji
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                // Publiczny dostęp do sprzętu (GET)
                .requestMatchers(HttpMethod.GET, "/api/equipment/**").permitAll()
                // Publiczny dostęp do rezerwacji (GET)
                .requestMatchers(HttpMethod.GET, "/api/reservations/**").permitAll()
                // Publiczny dostęp do tworzenia rezerwacji (POST)
                .requestMatchers(HttpMethod.POST, "/api/reservations").permitAll()
                // Wymagana rola ADMIN dla operacji modyfikujących sprzęt
                .requestMatchers(HttpMethod.POST, "/api/equipment").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/equipment/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/equipment/**").hasRole("ADMIN")
                // Wymagana rola ADMIN dla edycji i usuwania rezerwacji
                .requestMatchers(HttpMethod.PUT, "/api/reservations/*/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("ADMIN")
                // Pozostałe zapytania wymagają uwierzytelnienia
                .anyRequest().authenticated()
            )
            // Włączenie Basic Auth
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
