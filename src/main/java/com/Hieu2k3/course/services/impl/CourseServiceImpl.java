package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.course.CourseRequest;
import com.Hieu2k3.course.dtos.requests.course.UploadCourseRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.chapter.ChapterResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseChapterResponse;
import com.Hieu2k3.course.dtos.responses.course.CourseResponse;
import com.Hieu2k3.course.dtos.responses.course.UploadCourseResponse;
import com.Hieu2k3.course.dtos.responses.lesson.LessonResponse;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.enums.CourseLevel;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.CourseMapper;
import com.Hieu2k3.course.repository.CourseRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.CloudinaryService;
import com.Hieu2k3.course.services.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    CourseMapper courseMapper;
    UserRepository userRepository;
    CloudinaryService cloudinaryService;


    @Override
    public PageResponse<CourseResponse> getAllCourses(Specification<Course> spec, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Course> pageData = courseRepository.findAll(spec, pageable);

        List<CourseResponse> courseResponses  = pageData.getContent()
                .stream().map(course -> {

//                    List<Review> filteredComments = course.getComments().stream()
//                            .filter(r -> r.getRating() > 0 )
//                            .toList();

//                    long totalRating = filteredComments.stream()
//                            .mapToLong(Review::getRating)
//                            .sum();

//                    int numberOfValidReviews = filteredComments.size();
//                    double averageRating = numberOfValidReviews > 0 ? BigDecimal.valueOf((double) totalRating / numberOfValidReviews)
//                            .setScale(2, RoundingMode.HALF_UP)
//                            .doubleValue() : 0.0 ;

                    CourseResponse courseResponse = courseMapper.toCourseResponse(course);
//                    courseResponse.setAverageRating(averageRating);
                    return courseResponse;
                }).toList();

        return PageResponse.<CourseResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(courseResponses)
                .build();
    }

//    public Page<CourseResponse> getAllCoursesV2(String keyword,
//                                                Long points,
//                                                String language,
//                                                CourseLevel courseLevel,
//                                                Integer duration,
//                                                PageRequest pageRequest,
//                                                String sortField,
//                                                String sortDirection) {
//        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
//                ? Sort.Direction.DESC
//                : Sort.Direction.ASC;
//        Pageable pageable = PageRequest.of(
//                pageRequest.getPageNumber(),
//                pageRequest.getPageSize(),
//                direction, sortField
//        );
//        Page<Course> coursePage = courseRepository.searchProducts(keyword, points, pageable);
//        return null;
//
//    };


    @Override
    public List<String> getTitleSuggestions(String query) {
        return courseRepository.findTitleSuggestions(query);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public CourseResponse createCourse(CourseRequest request) {
        // check xem email trong spring security có hợp lệ hay không
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseMapper.toCourse(request);
        course.setAuthor(user);

        courseRepository.save(course);
        return courseMapper.toCourseResponse(course);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));

        return courseMapper.toCourseResponse(course);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public List<CourseResponse> myCourses() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Course> myCoures = courseRepository.findByAuthor(user);

        return myCoures.stream().map(courseMapper::toCourseResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    public UploadCourseResponse uploadCourse(UploadCourseRequest request, MultipartFile file, MultipartFile thumbnail) throws IOException {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String videoUrl = cloudinaryService.uploadVideoChunked(file, "courses").get("url").toString();
        String thumbnailUrl = cloudinaryService.uploadImage(thumbnail);

        Course course = courseMapper.updateCourse(request);
        course.setVideoUrl(videoUrl);
        course.setThumbnail(thumbnailUrl);
        course.setAuthor(user);

        courseRepository.save(course);

        return courseMapper.toUploadCourseResponse(course);
    }

    @Override
    public CourseChapterResponse getAllInfoCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));

        return courseMapper.toCourseChapterResponse(course);
    }

    @Override
    public CourseChapterResponse getAllInfoCourseV2(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));

        CourseChapterResponse response = courseMapper.toCourseChapterResponse(course);

        // Sắp xếp lessons trong mỗi chapter
        response.getChapters().forEach(chapter -> {
            List<LessonResponse> sortedLessons = chapter.getLessons().stream()
                    .sorted(Comparator.comparing(LessonResponse::getLessonId))
                    .collect(Collectors.toList());
            chapter.setLessons(sortedLessons);
        });

        // Sắp xếp chapters theo chapterId
        List<ChapterResponse> sortedChapters = response.getChapters().stream()
                .sorted(Comparator.comparing(ChapterResponse::getChapterId))
                .collect(Collectors.toList());
        response.setChapters(sortedChapters);
        return response;
    }
}
