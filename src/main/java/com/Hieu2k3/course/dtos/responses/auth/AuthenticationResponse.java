package com.Hieu2k3.course.dtos.responses.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {

    String token;

    String refreshToken;

    String role;

    boolean authenticated;
}
