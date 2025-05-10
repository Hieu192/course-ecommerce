package com.Hieu2k3.course.mapper;


import com.Hieu2k3.course.dtos.responses.chapter.ChapterResponse;
import com.Hieu2k3.course.dtos.responses.chapter.CreationChapterResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.entity.Chapter;
import com.Hieu2k3.course.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(target = "userId", source = "course.author.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    @Mapping(target = "chapterId", source = "id")
    @Mapping(target = "lessons", source = "lessons")
    CreationChapterResponse toCreateChapterResponse(Chapter chapter);

    // Đảm bảo mapping từ Lesson sang LessonDto
    @Mapping(source = "id", target = "lessonId")
    @Mapping(source = "lessonName", target = "lessonName")
    @Mapping(source = "videoUrl", target = "videoUrl")
    @Mapping(source = "description", target = "lessonDescription")
    CreationChapterResponse.LessonDto toLessonDto(Lesson lesson);

    @Mapping(target = "chapterId", source = "id")
    @Mapping(target = "lessons", source = "lessons")
    ChapterResponse toChapterResponse(Chapter chapter);

}
