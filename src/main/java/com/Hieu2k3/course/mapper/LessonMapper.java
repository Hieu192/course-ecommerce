package com.Hieu2k3.course.mapper;

import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.dtos.responses.lesson.UploadLessonResponse;
import com.Hieu2k3.course.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "chapterId", source = "chapter.id")
    UploadLessonResponse toUploadLessonResponse(Lesson lesson);

    LessonResponse toLessonResponse(Lesson lesson);
}
