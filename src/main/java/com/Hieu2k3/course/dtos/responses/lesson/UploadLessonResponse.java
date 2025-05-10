package com.Hieu2k3.course.dtos.responses.lesson;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadLessonResponse {
    Long courseId;
    Long chapterId;
    String chapterName;
    Long lessonId;
    String lessonName;
    String description;
    String videoUrl;
}
