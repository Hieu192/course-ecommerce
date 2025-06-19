package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.enrollment.EnrollmentRequest;
import com.Hieu2k3.course.dtos.responses.enrollment.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse buyCourse(EnrollmentRequest request);

    List<EnrollmentResponse> getCourseByUserCurrent();

}
