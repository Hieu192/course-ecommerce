package com.Hieu2k3.course.dtos.requests.course;

import com.Hieu2k3.course.enums.CourseLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRequest {
    String title;
    String description;
    Integer duration;
    String language;
    CourseLevel courseLevel;
    Long price;
    String thumbnail;
    String videoUrl;
}