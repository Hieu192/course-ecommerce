package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.review.ReviewRequest;
import com.Hieu2k3.course.dtos.requests.review.UpdateReviewRequest;
import com.Hieu2k3.course.dtos.responses.comment.DeleteCommentResponse;
import com.Hieu2k3.course.dtos.responses.review.ReviewResponse;
import com.Hieu2k3.course.dtos.responses.review.UpdateReviewResponse;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Review;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.ReviewMapper;
import com.Hieu2k3.course.repository.CourseRepository;
import com.Hieu2k3.course.repository.ReviewRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.ReviewService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    UserRepository userRepository;
    CourseRepository courseRepository;

    @Override
    public List<ReviewResponse> getReviewByCourse(Long id) {
        List<Review> allReviews = reviewRepository.findByCourseIdAndChapterIsNullAndLessonIsNull(id);
        return allReviews.stream()
                .filter(review -> review.getParentReview() == null)
                .map(reviewMapper::toReviewResponse)
                .toList();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ReviewResponse addReview(ReviewRequest reviewRequest, Long courseId) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));

        Review parentReview = null;
        if (reviewRequest.getParentReviewId() != null) {
            parentReview = reviewRepository.findById(reviewRequest.getParentReviewId())
                    .orElseThrow(() -> new AppException(ErrorCode.PARENT_COMMENT_NOT_EXISTED));
        }

        if ((reviewRequest.getContent() == null || reviewRequest.getContent().isEmpty()) && reviewRequest.getRating() == null) {
            throw new AppException(ErrorCode.INVALID_COMMENT_OR_RATING);
        }

        if (reviewRequest.getRating() != null && (reviewRequest.getRating() < 0 || reviewRequest.getRating() > 5)) {
            throw new AppException(ErrorCode.INVALID_RATING);
        }

//        if ( reviewRequest.getContent()!= null && bannedWordsService.containsBannedWords(reviewRequest.getContent())) {
//            throw new AppException(ErrorCode.INVALID_COMMENT_CONTENT);
//        }

        Review newReview = Review.builder()
                .user(user)
                .content(reviewRequest.getContent() != null && !reviewRequest.getContent().isEmpty()
                        ? reviewRequest.getContent()
                        : "")
                .rating(reviewRequest.getRating())
                .course(course)
                .parentReview(parentReview)
                .build();

        reviewRepository.save(newReview);

        return reviewMapper.toReviewResponse(newReview);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public DeleteCommentResponse deleteReviewById(Long id) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));

        if (Objects.equals(user.getId(), review.getUser().getId())) {
            reviewRepository.deleteById(id);
            return DeleteCommentResponse.builder()
                    .id(id)
                    .message("Delete Comment Successfully")
                    .build();
        }
        throw new AppException(ErrorCode.DELETE_COMMENT_INVALID);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public UpdateReviewResponse updateReview(Long id, UpdateReviewRequest request) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Review comment = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));

        if (Objects.equals(user.getId(), comment.getUser().getId())) {
//            if (request.getContent() != null && bannedWordsService.containsBannedWords(request.getContent())) {
//                throw new AppException(ErrorCode.INVALID_COMMENT_CONTENT);
//            }

//            if (request.getContent() != null) {
//                comment.setContent(request.getContent());
//            }
            reviewRepository.save(comment);

            return UpdateReviewResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .build();
        }

        throw new AppException(ErrorCode.UPDATE_COMMENT_INVALID);
    }
}
