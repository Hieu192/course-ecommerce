package com.Hieu2k3.course.controllers;

import com.Hieu2k3.course.dtos.requests.auth.AuthenticationRequest;
import com.Hieu2k3.course.dtos.requests.auth.IntrospectRequest;
import com.Hieu2k3.course.dtos.requests.auth.LogoutRequest;
import com.Hieu2k3.course.dtos.requests.auth.RefreshTokenRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.auth.AuthenticationResponse;
import com.Hieu2k3.course.dtos.responses.auth.IntrospectResponse;
import com.Hieu2k3.course.services.AuthenticationService;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController {

    AuthenticationService authenticationService;

//    @PostMapping("/outbound/authentication")
//    public ApiResponse<AuthenticationResponse> outboundAuthenticateGoogle(@RequestParam("code") String code) {
//        var result = authenticationService.outboundAuthenticate(code);
//        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
//    }

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {

        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {

        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Logout Successfully")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

}