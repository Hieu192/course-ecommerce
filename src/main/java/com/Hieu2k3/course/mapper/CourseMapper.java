package com.Hieu2k3.course.mapper;

import com.Hieu2k3.course.dtos.requests.course.CourseRequest;
import com.Hieu2k3.course.dtos.requests.course.UploadCourseRequest;
import com.Hieu2k3.course.dtos.responses.chapter.ChapterResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseChapterResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseResponse;
import com.Hieu2k3.course.dtos.responses.course.UploadCourseResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.entity.Chapter;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(source = "author.name", target = "author")
    @Mapping(target = "averageRating", ignore = true)
    CourseResponse toCourseResponse(Course course);

    @Mapping(source = "course.title", target = "courseName")
    CourseChapterResponse toCourseChapterResponse(Course course);

    @Mapping(source = "id", target = "chapterId")
    ChapterResponse toChapterResponse(Chapter chapter);

    @Mapping(source = "id", target = "lessonId")
    LessonResponse toLessonResponse(Lesson lesson);

    @Mapping(source = "author.name", target = "author")
    UploadCourseResponse toUploadCourseResponse(Course course);

    Course updateCourse(UploadCourseRequest request);

    Course toCourse(CourseRequest request);



}
