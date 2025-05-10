package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.lesson.LessonProgressRequest;
import com.Hieu2k3.course.dtos.requests.lesson.UploadLessonRequest;
import com.Hieu2k3.course.dtos.responses.lesson.LessonProgressResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.dtos.responses.lesson.UploadLessonResponse;
import com.Hieu2k3.course.entity.Lesson;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LessonService {
    UploadLessonResponse createLesson(UploadLessonRequest request, MultipartFile lessonVideo) throws IOException;

    List<LessonResponse> getLessonsByChapter(Long id);

    LessonProgressResponse markLessonAsCompleted(LessonProgressRequest request);

//    UserCompletionResponse calculateCompletion(Long courseId);
}
