package com.Hieu2k3.course.mapper;

import com.Hieu2k3.course.dtos.responses.enrollment.EnrollmentResponse;
import com.Hieu2k3.course.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "courseLevel", source = "course.courseLevel")
    @Mapping(target = "courseId", source ="course.id")
    @Mapping(target = "title", source = "course.title")
    @Mapping(target = "points", source = "course.points")
    @Mapping(target = "author", source = "course.author.name")
    @Mapping(target = "thumbnail", source = "course.thumbnail")
    @Mapping(target = "createAt", source = "course.createdAt")
    EnrollmentResponse toBuyCourseResponse(Enrollment enrollment);
}
