package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.chapter.CreationChapterRequest;
import com.Hieu2k3.course.dtos.responses.chapter.ChapterResponse;
import com.Hieu2k3.course.dtos.responses.chapter.CreationChapterResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChapterService {
    CreationChapterResponse createChapter(CreationChapterRequest request, MultipartFile lessonVideo) throws IOException;

    List<ChapterResponse> getChaptersByCourse(Long id);
}
