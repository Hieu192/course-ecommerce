package com.Hieu2k3.course.controllers;

import com.Hieu2k3.course.dtos.requests.course.CourseRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseChapterResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseResponse;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.turkraft.springfilter.boot.Filter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseController {

    CourseService courseService;

    /**
     * http://localhost:8080/api/v1/courses?filter= (duration > 100) and courseLevel: 'BEGINNER' and title ~~ '2' and
     * @param spec
     * @param page
     * @param size
     * @param request
     * @return
     */
    @GetMapping("")
    ApiResponse<PageResponse<CourseResponse>> getAllCourses(
            @Filter Specification<Course> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            HttpServletRequest request) {
        String rawFilter = request.getParameter("filter");
        log.info("Received filter: {}", rawFilter);

        PageResponse<CourseResponse> result = courseService.getAllCourses(spec, page, size);

        return ApiResponse.<PageResponse<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get All Courses Successfully")
                .result(result)
                .build();
    }

    @PostMapping("")
    ApiResponse<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest){
        var result = courseService.createCourse(courseRequest);

        return ApiResponse.<CourseResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Course Successfully")
                .result(result)
                .build();
    }

    @GetMapping("/my-courses")
    ApiResponse<List<CourseResponse>> myCourses(){
        var result = courseService.myCourses();

        return ApiResponse.<List<CourseResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @GetMapping("/info-course/{id}")
    ApiResponse<CourseChapterResponse> infoCourse(@PathVariable Long id){
        return ApiResponse.<CourseChapterResponse>builder()
                .code(HttpStatus.OK.value())
                .result(courseService.getAllInfoCourseV2(id))
                .build();
    }

}
