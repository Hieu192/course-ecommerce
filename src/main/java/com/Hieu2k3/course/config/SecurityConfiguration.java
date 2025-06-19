package com.Hieu2k3.course.config;

import com.Hieu2k3.course.security.CustomJwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomJwtDecoder jwtDecoder;

    public static final String[] PUBLIC_ENDPOINT = {
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/v1/auth/token",
            "/api/v1/register",
            "/api/v1/auth/logout",
            "/api/v1/auth/introspect",
            "/api/v1/auth/refresh",
            "/api/v1/auth/outbound/authentication",
            "/api/v1/create-password",
            "/api/v1/send-otp",
            "/api/v1/reset-password",
            "/api/v1/verify-otp",
            "/api/v1/check-exists-user",
            "/api/v1/send-otp-register",
            "/api/v1/courses/**",
            "/api/v1/course/{id}",
            "/api/v1/info-course/{id}",
            "/ws/**",
            "/upload/**",
            "/api/v1/courses-review/{courseId}",
            "/api/v1/payment/vn-pay-callback",
            "/api/v1/payment/vn-pay/**",
            "/api/v1/get-ads-active",
            "/api/v1/user/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // tắt CSRF (vì dùng jwt)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN") // Chỉ cho phép quyền ADMIN truy cập vào /api/v1/admin/**
                        .anyRequest().authenticated())
//                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())) // không có token mà cần xác thực thì đc gọi trả về lỗi
//                .addFilterBefore(new CustomJwtAuthFilter(), BearerTokenAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // không dùng session

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // chuyển JWT thành Authentication và scope
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter(); // chọn claim scope chứa quyền
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // bỏ "SCOPE_"

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter(); // tạo Authentication
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
