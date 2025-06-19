package com.Hieu2k3.course.controllers;


import com.Hieu2k3.course.dtos.requests.review.ReviewRequest;
import com.Hieu2k3.course.dtos.requests.review.UpdateReviewRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.comment.DeleteCommentResponse;
import com.Hieu2k3.course.dtos.responses.review.ReviewResponse;
import com.Hieu2k3.course.dtos.responses.review.UpdateReviewResponse;
import com.Hieu2k3.course.services.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/review")
@Slf4j
public class ReviewController {

    ReviewService reviewService;

    @GetMapping("/courses-review/{courseId}")
    ApiResponse<List<ReviewResponse>> getReviewByCourseId(@PathVariable Long courseId){
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.getReviewByCourse(courseId))
                .build();
    }

    @PostMapping("/add-review")
    ApiResponse<ReviewResponse> addReview(@RequestBody @Valid ReviewRequest reviewRequest, @RequestParam Long id){
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(reviewService.addReview(reviewRequest, id))
                .build();
    }

    @DeleteMapping("/delete-review/{id}")
    ApiResponse<DeleteCommentResponse> deleteReview(@PathVariable Long id) {
        return ApiResponse.<DeleteCommentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.deleteReviewById(id))
                .build();
    }

    @PutMapping("/update-review/{id}")
    ApiResponse<UpdateReviewResponse> updateReview(@PathVariable Long id, @RequestBody UpdateReviewRequest request) {
        return ApiResponse.<UpdateReviewResponse>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.updateReview(id, request))
                .build();
    }
}
