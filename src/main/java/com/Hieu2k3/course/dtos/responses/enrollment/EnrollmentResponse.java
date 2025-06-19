package com.Hieu2k3.course.dtos.responses.enrollment;

import com.Hieu2k3.course.enums.CourseLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentResponse {
    Long courseId;
    String title;
    String author;
    CourseLevel courseLevel;
    String thumbnail;
    Long points;
    LocalDateTime createAt;
}
