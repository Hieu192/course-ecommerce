package com.Hieu2k3.course.dtos.responses.lesson;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonResponse {

    Long lessonId;
    String name;
    String videoUrl;
    String description;

}
