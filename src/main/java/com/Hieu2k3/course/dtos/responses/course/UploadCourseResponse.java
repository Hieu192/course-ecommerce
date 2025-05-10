package com.Hieu2k3.course.dtos.responses.course;

import com.Hieu2k3.course.enums.CourseLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadCourseResponse {

    String author;
    String title;
    String description;
    CourseLevel courseLevel;
    Integer duration;
    BigDecimal price;
    String thumbnail;
    String videoUrl;
}