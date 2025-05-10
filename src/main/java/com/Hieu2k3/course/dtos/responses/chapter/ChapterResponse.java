package com.Hieu2k3.course.dtos.responses.chapter;

import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterResponse {

    Long chapterId;
    String chapterName;
    String description;
    List<LessonResponse> lessons;
}
