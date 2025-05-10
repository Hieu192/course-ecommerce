package com.Hieu2k3.course.dtos.requests.user;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {

    String email;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String firstName;

    String lastName;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    LocalDate dob;
}