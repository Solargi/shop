package com.example.shop.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        //overwrite default authentication
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/v1/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "configuration/ui",
                                "configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/swagger-ui.html/**",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/items").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/items").hasAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/items").hasAuthority("ROLE_admin")


                        //everything else you need to be authenticated
                        .anyRequest().authenticated()

                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    //returns password encoder
    // with 2^12 hashing iterations
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder(12);
    }
}
