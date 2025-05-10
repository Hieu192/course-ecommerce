package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.auth.AuthenticationRequest;
import com.Hieu2k3.course.dtos.requests.auth.IntrospectRequest;
import com.Hieu2k3.course.dtos.requests.auth.LogoutRequest;
import com.Hieu2k3.course.dtos.requests.auth.RefreshTokenRequest;
import com.Hieu2k3.course.dtos.responses.auth.AuthenticationResponse;
import com.Hieu2k3.course.dtos.responses.auth.IntrospectResponse;
import com.Hieu2k3.course.dtos.responses.auth.LoginResponse;
import com.Hieu2k3.course.entity.RefreshToken;
import com.Hieu2k3.course.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.security.SignedObject;
import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    void logout(LogoutRequest request) throws ParseException, JOSEException ;

    AuthenticationResponse outboundAuthenticate(String code);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    String generateToken(User user);

    String generateRefreshToken(User user, boolean isMobile);

    RefreshToken verifyRefreshToken(String refreshToken);

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException;

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    String buildScope(User user);
}
