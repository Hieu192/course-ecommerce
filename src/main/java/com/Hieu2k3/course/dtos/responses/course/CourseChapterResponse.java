package com.Hieu2k3.course.dtos.responses.course;

import com.Hieu2k3.course.dtos.responses.chapter.ChapterResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseChapterResponse {

    Long id;
    String courseName;
    String description;
    List<ChapterResponse> chapters;
}
