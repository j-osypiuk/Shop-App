package com.example.shopapp.security;

import static com.example.shopapp.user.Role.*;
import com.example.shopapp.user.UserRepository;
import com.example.shopapp.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl(
                userRepository,
                passwordEncoder()
        );
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserAccessDecisionManager decisionManager) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/users/customer").permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/photos",
                                        "/products",
                                        "/discounts",
                                        "/category").permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/users/{id}",
                                        "/orders/user/{id}").access(decisionManager)
                                .requestMatchers(HttpMethod.PUT, "/users/{id}").access(decisionManager)
                                .requestMatchers(HttpMethod.PATCH, "/users/{id}").access(decisionManager)
                                .requestMatchers(HttpMethod.POST, "/orders/{id}").access(decisionManager)
                                .requestMatchers(
                                        "/users/**",
                                        "/photos/product/**",
                                        "/products/**",
                                        "/orders/**",
                                        "/discounts/**",
                                        "/category/**",
                                        "/address")
                                .hasAnyAuthority(ADMIN.name(), EMPLOYEE.name())
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
