package com.Hieu2k3.course.dtos.requests.enrollment;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentRequest {

    @NotNull(message = "COURSE_ID_INVALID")
    Long courseId;
}
