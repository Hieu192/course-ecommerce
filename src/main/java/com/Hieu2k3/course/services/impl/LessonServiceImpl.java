package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.lesson.LessonProgressRequest;
import com.Hieu2k3.course.dtos.requests.lesson.UploadLessonRequest;
import com.Hieu2k3.course.dtos.responses.lesson.LessonProgressResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.dtos.responses.lesson.UploadLessonResponse;
import com.Hieu2k3.course.entity.*;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.LessonMapper;
import com.Hieu2k3.course.repository.*;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.CloudinaryService;
import com.Hieu2k3.course.services.LessonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    UserRepository userRepository;
    EnrollmentRepository enrollmentRepository;
    LessonProgressRepository lessonProgressRepository;


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
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public LessonProgressResponse markLessonAsCompleted(LessonProgressRequest request) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_EXIST));

        Course course = lesson.getChapter().getCourse();
        if (!enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new AppException(ErrorCode.COURSE_ACCESS_DENIED);
        }

        LessonProgress existingProgress = lessonProgressRepository.findByUserAndLesson(user, lesson);
        if (existingProgress != null) {
            return LessonProgressResponse.builder()
                    .lessonId(existingProgress.getLesson().getId())
                    .lessonName(existingProgress.getLesson().getLessonName())
                    .isComplete(existingProgress.getCompleted())
                    .build();
        }

        lessonProgressRepository.save(LessonProgress.builder()
                .user(user)
                .lesson(lesson)
                .completed(true)
                .build());

        return LessonProgressResponse.builder()
                .lessonId(lesson.getId())
                .lessonName(lesson.getLessonName())
                .isComplete(true)
                .build();
    }
}
