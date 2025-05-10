package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.user.TeacherRegisterRequest;
import com.Hieu2k3.course.dtos.responses.user.InfoTeacherResponse;
import com.Hieu2k3.course.dtos.responses.user.TeacherResponse;

import java.util.List;

public interface TeacherService {
    List<TeacherResponse> getAll();

    TeacherResponse registerTeacher(TeacherRegisterRequest request);

    TeacherResponse agreeTeacher(Long id);

    TeacherResponse rejectTeacher(Long id);

    InfoTeacherResponse getInfoTeacherByCourse(Long id);
}
