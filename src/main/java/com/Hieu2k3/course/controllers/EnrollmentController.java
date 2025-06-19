package com.Hieu2k3.course.controllers;


import com.Hieu2k3.course.dtos.requests.enrollment.EnrollmentRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.enrollment.EnrollmentResponse;
import com.Hieu2k3.course.services.EnrollmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
@Slf4j
public class EnrollmentController {

    EnrollmentService enrollmentService;

    @PostMapping("/buy-course")
    ApiResponse<EnrollmentResponse> buyCourse(@RequestBody @Valid EnrollmentRequest request) {
        return ApiResponse.<EnrollmentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(enrollmentService.buyCourse(request))
                .build();
    }

    @GetMapping("/users/me/courses")
    ApiResponse<List<EnrollmentResponse>> getCourseByCurrentUser() {
        return ApiResponse.<List<EnrollmentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("My Courses")
                .result(enrollmentService.getCourseByUserCurrent())
                .build();
    }

}
