package com.Hieu2k3.course.dtos.requests.lesson;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadLessonRequest {

    Long courseId;
    Long chapterId;
    String lessonName;
    String description;

}
