package com.Univauction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST면 보통 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/wallets/{userId}/enroll").permitAll()
                        .requestMatchers("/ideas-with-auction").permitAll()
                        .requestMatchers("/auctions").permitAll()
                        .requestMatchers("/auctions/{auctionId}/bids").permitAll()
                        .requestMatchers("/auctions/{auctionId}").permitAll()
                        .requestMatchers("/auctions/bids/{auctionId}").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
