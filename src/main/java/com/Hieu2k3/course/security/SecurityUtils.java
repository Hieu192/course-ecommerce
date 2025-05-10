package com.Hieu2k3.course.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUtils {
    // lấy username
    // 1. lấy SecurityContext hiện tại từ SecurityContextHolder
    // 2. Lấy Authentication từ context.
    // 3. Truyền Authentication vào method extractPrincipal() để lấy thông tin user.
    public static Optional<String> getCurrentUserLogin(){
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(contextHolder.getAuthentication()));
    }

    // Lấy thông tin username từ đối tượng Authentication
    // Authentication.getPrincipal() có thể là:
    //  - UserDetails: khi dùng UsernamePasswordAuthenticationToken
    //  - Jwt: khi dùng JWT qua BearerTokenAuthenticationFilter
    //  - String: tùy cách cấu hình, đôi khi principal có thể chỉ là chuỗi username
    private static String extractPrincipal(Authentication authentication){
        if(authentication == null){
            return null;
        }else if(authentication.getPrincipal() instanceof UserDetails userDetails){
            return userDetails.getUsername();
        }else if(authentication.getPrincipal() instanceof Jwt jwt){
            return jwt.getSubject();
        }else if(authentication.getPrincipal() instanceof String s){
            return s;
        }
        return null;
    }

    // Lấy ra access token JWT (dạng chuỗi) đã được xác thực ở request hiện tại.
    public static Optional<String> getCurrentUserJwt(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }
}
