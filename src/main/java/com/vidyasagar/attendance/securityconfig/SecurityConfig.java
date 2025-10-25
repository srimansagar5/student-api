package com.vidyasagar.attendance.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    // Define in-memory users
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails faculty = User.withUsername("faculty")
                .password(encoder.encode("faculty123"))
                .roles("FACULTY")
                .build();

        return new InMemoryUserDetailsManager(admin, faculty);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ Permit Swagger and OpenAPI endpoints
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()
                        // ✅ Allow home or health endpoints (optional)
                        .requestMatchers("/").permitAll()
                        // Role-based access for APIs
                        .requestMatchers("/api/v1/students/**").hasAnyRole("ADMIN", "FACULTY")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/internal/**").denyAll()
                        // ✅ Everything else requires authentication
                        .anyRequest().authenticated()
                )
                // ✅ Enable HTTP Basic Auth
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                        .xssProtection(xss -> xss.disable())
                        .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
                );

        return httpSecurity.build();
    }
}

