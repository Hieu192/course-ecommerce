package com.Hieu2k3.course.controllers;

import com.Hieu2k3.course.dtos.requests.chapter.CreationChapterRequest;
import com.Hieu2k3.course.dtos.requests.lesson.LessonProgressRequest;
import com.Hieu2k3.course.dtos.requests.lesson.UploadLessonRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.chapter.CreationChapterResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonProgressResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.dtos.responses.lesson.UploadLessonResponse;
import com.Hieu2k3.course.services.LessonService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/lesson")
@Slf4j
public class LessonController {

    LessonService lessonService;

    @PostMapping("/upload")
    ApiResponse<UploadLessonResponse> createLesson(@RequestPart("lesson") @Valid UploadLessonRequest request,
                                                   @RequestPart("file") MultipartFile file) throws IOException {
        return ApiResponse.<UploadLessonResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(lessonService.createLesson(request, file))
                .build();
    }

    @GetMapping("/{chapterId}")
    ApiResponse<List<LessonResponse>> getLessonByChapter(@PathVariable Long chapterId) {
        return ApiResponse.<List<LessonResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(lessonService.getLessonsByChapter(chapterId))
                .build();
    }

    @PostMapping("/update-lesson-progress")
    ApiResponse<LessonProgressResponse> markLessonAsCompleted(@RequestBody LessonProgressRequest request) {
        return ApiResponse.<LessonProgressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update successfully")
                .result(lessonService.markLessonAsCompleted(request))
                .build();
    }
}
