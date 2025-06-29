package com.Hieu2k3.course.controllers;

import com.Hieu2k3.course.dtos.requests.chapter.CreationChapterRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.chapter.ChapterResponse;
import com.Hieu2k3.course.dtos.responses.chapter.CreationChapterResponse;
import com.Hieu2k3.course.services.ChapterService;
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
@RequestMapping("/api/v1/chapter")
@Slf4j
public class ChapterController {
    ChapterService chapterService;

    @PostMapping("/upload")
    ApiResponse<CreationChapterResponse> createChapter(@RequestPart("chapter") @Valid CreationChapterRequest request,
                                                      @RequestPart("file") MultipartFile file) throws IOException {
        return ApiResponse.<CreationChapterResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(chapterService.createChapter(request, file))
                .build();
    }

    @GetMapping("/{courseId}")
    ApiResponse<List<ChapterResponse>> getChapterByCourse(@PathVariable Long courseId) throws IOException {
        return ApiResponse.<List<ChapterResponse>>builder()
                .code(HttpStatus.CREATED.value())
                .result(chapterService.getChaptersByCourse(courseId))
                .build();
    }
}
