package com.ecommerce.payment_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.payment_service.security.JwtAuthFilter;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	 private final JwtAuthFilter jwtAuthFilter;

	    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
	        this.jwtAuthFilter = jwtAuthFilter;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        return http
	                .csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(req -> req
	                        .requestMatchers("/api/payment/**").hasRole("USER")
	                        .requestMatchers(
	                                "/v3/api-docs/**",
	                                "/swagger-ui/**",
	                                "/swagger-ui.html"
	                            ).permitAll()
	                        .anyRequest().authenticated()
	                )
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	                .build();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
	        return configuration.getAuthenticationManager();
	    }
}

