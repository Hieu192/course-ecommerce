package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.auth.AuthenticationRequest;
import com.Hieu2k3.course.dtos.requests.auth.IntrospectRequest;
import com.Hieu2k3.course.dtos.requests.auth.LogoutRequest;
import com.Hieu2k3.course.dtos.requests.auth.RefreshTokenRequest;
import com.Hieu2k3.course.dtos.responses.auth.AuthenticationResponse;
import com.Hieu2k3.course.dtos.responses.auth.IntrospectResponse;
import com.Hieu2k3.course.dtos.responses.auth.LoginResponse;
import com.Hieu2k3.course.entity.Permission;
import com.Hieu2k3.course.entity.RefreshToken;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.exception.ExpiredTokenException;
import com.Hieu2k3.course.exception.InvalidTokenException;
import com.Hieu2k3.course.repository.TokenRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.services.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
//    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);
        var refreshToken = generateRefreshToken(user, request.getIsMobile());

        String role = user.getRole().getName();

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .role(role)
                .authenticated(true)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            RefreshToken signRefreshToken = verifyRefreshToken(request.getRefreshToken());

            if (signRefreshToken != null) {
                signRefreshToken.setRevoked(true);
                tokenRepository.save(signRefreshToken);
            } else {
                log.info("Token has already been invalidated");
            }

        } catch (AppException exception) {
            log.info("Token already expired or invalid");
        }
    }

    @Override
    public AuthenticationResponse outboundAuthenticate(String code) {
        return null;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken  = verifyRefreshToken(request.getRefreshToken());

        //check refreshToken isExpired
        if (refreshToken .getRefreshExpirationTime().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        tokenRepository.delete(refreshToken);

        // generate new token by refreshToken
        return AuthenticationResponse.builder()
                .token(generateToken(refreshToken.getUser()))
                .refreshToken(generateRefreshToken(refreshToken.getUser(), refreshToken.isMobile()))
                .build();
    }

    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("course.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli())
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateRefreshToken(User user, boolean isMobile) {
        List<RefreshToken> userTokens = tokenRepository.findByUser(user);

        int tokenCount = userTokens.size();

        if (tokenCount >= MAX_TOKENS) {
            boolean hasNoMobileToken = userTokens.stream().anyMatch(refreshToken -> !refreshToken.isMobile());
            RefreshToken tokenToDelete;
            if (hasNoMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken(UUID.randomUUID().toString())
                .refreshExpirationTime(Instant.now().plusMillis(expirationRefreshToken))
                .revoked(false)
                .expired(false)
                .isMobile(isMobile)
                .build();
        tokenRepository.save(refreshToken);
        return refreshToken.getRefreshToken();
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (token.getRefreshExpirationTime().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        if(token.isRevoked()) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }

        return token;
    }

    @Override
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        if (token == null || token.trim().isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()); //kiểm tra chữ ký

        SignedJWT signedJWT = SignedJWT.parse(token); // giải mã

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (expiryTime.before(new Date())) {
            throw new ExpiredTokenException();
        }

        var verified = signedJWT.verify(verifier); // xác thực chữ ký
        if (!verified) throw new InvalidTokenException();

        return signedJWT;
    }

    // kiểm tra token có hợp lệ hay không và trả về scope
    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;
        String scope =  null;
        try {
            SignedJWT signedJWT = verifyToken(token);
            scope = (String) signedJWT.getJWTClaimsSet().getClaim("scope");
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .scope(scope)
                .build();
    }

    // gắn phạm vi, quyền cho scope trong token
    @Override
    public String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        Optional.ofNullable(user.getRole()).ifPresent(role -> {
            joiner.add(role.getName());

            Optional.ofNullable(role.getPermissions()).ifPresent(permissions ->
                    permissions.forEach(permission -> joiner.add(permission.getName())));
        });
        return joiner.toString();
    }
}
