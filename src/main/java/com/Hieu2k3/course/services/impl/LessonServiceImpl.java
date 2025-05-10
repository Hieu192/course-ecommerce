package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.lesson.LessonProgressRequest;
import com.Hieu2k3.course.dtos.requests.lesson.UploadLessonRequest;
import com.Hieu2k3.course.dtos.responses.lesson.LessonProgressResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.dtos.responses.lesson.UploadLessonResponse;
import com.Hieu2k3.course.entity.Chapter;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Lesson;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.LessonMapper;
import com.Hieu2k3.course.repository.ChapterRepository;
import com.Hieu2k3.course.repository.CourseRepository;
import com.Hieu2k3.course.repository.LessonRepository;
import com.Hieu2k3.course.services.CloudinaryService;
import com.Hieu2k3.course.services.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LessonServiceImpl implements LessonService {

    LessonRepository lessonRepository;
    ChapterRepository chapterRepository;
    CloudinaryService cloudinaryService;
    CourseRepository courseRepository;
    LessonMapper lessonMapper;


    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    public UploadLessonResponse createLesson(UploadLessonRequest request, MultipartFile lessonVideo) throws IOException {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));

        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));

        if (!chapter.getCourse().getId().equals(course.getId())) {
            throw new AppException(ErrorCode.CHAPTER_NOT_IN_COURSE);
        }

        Map<String, Object> uploadResult = cloudinaryService
                .uploadVideoChunked(lessonVideo,
                        "courses/" + course.getId() + "/chapters/" + chapter.getId());
        String videoUrl = Optional.ofNullable(uploadResult.get("secure_url"))
                .orElse(uploadResult.get("url"))
                .toString();

        Lesson lesson = Lesson.builder()
                .chapter(chapter)
                .contentType("video")
                .videoUrl(videoUrl)
                .lessonName(request.getLessonName())
                .description(request.getDescription())
                .build();
        Lesson saved = lessonRepository.save(lesson);
        chapter.getLessons().add(saved);
        return lessonMapper.toUploadLessonResponse(saved);
    }

    @Override
    public List<LessonResponse> getLessonsByChapter(Long id) {
        List<Lesson> lessons = lessonRepository.findByChapterId(id);
        return lessons.stream()
                .map(lessonMapper::toLessonResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LessonProgressResponse markLessonAsCompleted(LessonProgressRequest request) {
        return null;
    }
}
