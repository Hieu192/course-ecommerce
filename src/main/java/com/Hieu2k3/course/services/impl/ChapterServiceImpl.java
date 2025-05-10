package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.chapter.CreationChapterRequest;
import com.Hieu2k3.course.dtos.responses.chapter.ChapterResponse;
import com.Hieu2k3.course.dtos.responses.chapter.CreationChapterResponse;
import com.Hieu2k3.course.entity.Chapter;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Lesson;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.ChapterMapper;
import com.Hieu2k3.course.repository.ChapterRepository;
import com.Hieu2k3.course.repository.CourseRepository;
import com.Hieu2k3.course.repository.LessonRepository;
import com.Hieu2k3.course.services.ChapterService;
import com.Hieu2k3.course.services.CloudinaryService;
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
public class ChapterServiceImpl implements ChapterService  {

    CourseRepository courseRepository;
    ChapterRepository chapterRepository;
    CloudinaryService cloudinaryService;
    LessonRepository lessonRepository;
    ChapterMapper chapterMapper;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    public CreationChapterResponse createChapter(CreationChapterRequest request, MultipartFile lessonVideo) throws IOException {

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));

        Chapter chapter = chapterRepository
                .findByChapterNameAndCourse(request.getChapterName(), course)
                .orElseGet(() -> {
                    Chapter newChapter = Chapter.builder()
                            .course(course)
                            .chapterName(request.getChapterName())
                            .description(request.getDescription())
                            .build();
                    return chapterRepository.save(newChapter);
                });

        Map<String, Object> uploadResult = cloudinaryService
                .uploadVideoChunked(lessonVideo, "courses/" + course.getId() + "/chapters/" + chapter.getId());
        String videoUrl = Optional.ofNullable(uploadResult.get("secure_url"))
                .orElse(uploadResult.get("url"))
                .toString();

        // 4. Tạo Lesson mới gắn với chapter
        Lesson lesson = Lesson.builder()
                .chapter(chapter)
                .contentType("video")
                .videoUrl(videoUrl)
                .lessonName(request.getLessonName())
                .description(request.getLessonDescription())
                .build();
        lessonRepository.save(lesson);
        chapter.getLessons().add(lesson);
        return chapterMapper.toCreateChapterResponse(chapter);
    }

    @Override
    public List<ChapterResponse> getChaptersByCourse(Long id) {
        List<Chapter> chapters = chapterRepository.findByCourseId(id);
        return chapters.stream()
                .map(chapterMapper::toChapterResponse)
                .collect(Collectors.toList());
    }
}
