package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.review.ReviewRequest;
import com.Hieu2k3.course.dtos.requests.review.UpdateReviewRequest;
import com.Hieu2k3.course.dtos.responses.comment.DeleteCommentResponse;
import com.Hieu2k3.course.dtos.responses.review.ReviewResponse;
import com.Hieu2k3.course.dtos.responses.review.UpdateReviewResponse;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getReviewByCourse(Long id);

    ReviewResponse addReview(ReviewRequest reviewRequest, Long courseId );

    DeleteCommentResponse deleteReviewById(Long id);

    UpdateReviewResponse updateReview(Long id, UpdateReviewRequest request);
}
