package com.Hieu2k3.course.dtos.requests.lesson;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonProgressRequest {

    Long lessonId;
}