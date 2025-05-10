package com.Hieu2k3.course.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "refresh_token", nullable = false)
    String refreshToken;

    @Column(name = "refresh_expiration_time", nullable = false)
    Instant refreshExpirationTime; // thời gian sống của refresh token

    @Column(name = "revoked", nullable = false)
    boolean revoked;

    @Column(name = "expired", nullable = false)
    boolean expired;

    @Column(name = "is_mobile")
    boolean isMobile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
