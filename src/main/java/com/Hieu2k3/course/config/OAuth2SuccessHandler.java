package com.Hieu2k3.course.config;

import com.Hieu2k3.course.dtos.responses.auth.AuthenticationResponse;
import com.Hieu2k3.course.entity.Role;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.repository.RoleRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.services.AuthenticationService;
import com.Hieu2k3.course.utils.RoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        assert name != null;
        String[] nameParts = name.split(" ");
        String firstname = nameParts[0];
        String lastname = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";

        Role roles = roleRepository.findByName(RoleType.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        User user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(User.builder()
                .email(email)
                .firstName(firstname)
                .lastName(lastname)
                .name(name)
                .role(roles)
                .build()));

        String token = authenticationService.generateToken(user);

        AuthenticationResponse resp = AuthenticationResponse.builder()
                .token(token)
                .build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(resp));
    }

}