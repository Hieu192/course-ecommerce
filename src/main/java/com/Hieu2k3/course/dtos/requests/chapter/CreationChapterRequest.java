package com.Hieu2k3.course.dtos.requests.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationChapterRequest {

    @NotNull(message = "COURSE_ID_INVALID")
    Long courseId;

    @NotBlank(message = "CHAPTER_NAME_INVALID")
    String chapterName;

    @NotBlank(message = "LESSON_NAME_INVALID")
    String lessonName;

    String description;

    String lessonDescription;

}