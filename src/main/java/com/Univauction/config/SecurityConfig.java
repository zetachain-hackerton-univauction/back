package com.Univauction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // 아래 corsConfigurationSource() 사용
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT/REST라면 권장
                .authorizeHttpRequests(auth -> auth
                        // 프리플라이트 허용(중요)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 헬스체크(배포 확인용)
                        .requestMatchers("/actuator/health").permitAll()

                        // 로그인/공개 API
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/wallets/*/enroll").permitAll()

                        // 공개 조회 API들 (변수 부분은 * / ** 로)
                        .requestMatchers(HttpMethod.GET, "/ideas-with-auction").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auctions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auctions/*/bids").permitAll()

                        // 나머지는 인증
                        .anyRequest().authenticated()
                );

        // 데모용으로 httpBasic()을 잠깐 켜고 싶다면 아래 라인 주석 해제
        // .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // CORS: 프론트 도메인 정확히 넣기 (급하면 "*" 가능)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "https://<앱_배포_주소>",         // 예: https://example.com
                "https://<백엔드>.onrender.com" // 필요 시
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Location"));
        cfg.setAllowCredentials(true); // 쿠키/세션 사용 시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
