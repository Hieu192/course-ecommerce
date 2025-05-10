package com.Hieu2k3.course.dtos.responses.user;

import com.Hieu2k3.course.enums.RegistrationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherResponse {

    Long id;
    String email;
    String name;
    String phone;

    String expertise;
    Double yearsOfExperience;
    String bio;
    String facebookLink;
    String certificate;
    String cvUrl;

    RegistrationStatus registrationStatus;

}
