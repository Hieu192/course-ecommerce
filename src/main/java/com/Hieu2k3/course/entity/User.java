package com.Hieu2k3.course.entity;

import com.Hieu2k3.course.enums.Gender;
import com.Hieu2k3.course.enums.RegistrationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class User extends AbstractEntity<Long> {

    @Column(name = "email", nullable = false, unique = true)
    @NotNull
    @Email
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "avatar")
    String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @Column(name = "phone")
    String phone;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    LocalDate dob;

    @Column(name = "otp")
    String otp;

    @Column(name = "otp_expiry_date")
    LocalDateTime otpExpiryDate;

    @Column(name = "address")
    String address;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

    @Column(name = "zipCode")
    String zipCode;

    @Column(name = "expertise")
    String expertise;

    @Column(name = "yearsOfExperience")
    Double yearsOfExperience;

    @Column(name = "bio")
    String bio;

    @Column(name = "certificate")
    String certificate;

    @Column(name = "cvUrl")
    String cvUrl;

    @Column(name = "facebookLink")
    String facebookLink;

    @Column(name = "googleLink")
    String googleLink;

    @Column(name = "points", columnDefinition = "BIGINT DEFAULT 0")
    Long points;

    @Column(name = "registrationStatus")
    @Enumerated(EnumType.STRING)
    RegistrationStatus registrationStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reviews"})
    Set<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Favorite> favorites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonInclude
    List<Post> posts;

    @Column(name = "enabled")
    Boolean enabled;

    @PrePersist
    protected void onCreate() {
        if (enabled == null) {
            enabled = Boolean.TRUE;
        }
    }
}
