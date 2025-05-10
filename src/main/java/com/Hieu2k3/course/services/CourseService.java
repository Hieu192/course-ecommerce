package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.course.CourseRequest;
import com.Hieu2k3.course.dtos.requests.course.UploadCourseRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseChapterResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseResponse;
import com.Hieu2k3.course.dtos.responses.course.UploadCourseResponse;
import com.Hieu2k3.course.entity.Course;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    PageResponse<CourseResponse> getAllCourses(Specification<Course> spec, int page, int size);

    List<String> getTitleSuggestions(String query);

    CourseResponse createCourse(CourseRequest request);

    CourseResponse getCourseById(Long id);

    List<CourseResponse> myCourses();

    UploadCourseResponse uploadCourse(UploadCourseRequest request, MultipartFile file, MultipartFile thumbnail) throws IOException;

    CourseChapterResponse getAllInfoCourse(Long courseId);

    CourseChapterResponse getAllInfoCourseV2(Long courseId);
}
