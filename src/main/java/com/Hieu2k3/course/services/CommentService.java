package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.comment.CommentRequest;
import com.Hieu2k3.course.dtos.requests.comment.UpdateCommentRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.comment.CommentResponse;
import com.Hieu2k3.course.dtos.responses.comment.UpdateCommentResponse;

public interface CommentService {
    PageResponse<CommentResponse> getCommentByPostId(Long postId, int page, int size);

    CommentResponse addCommentPost(CommentRequest request);

    void deleteCommentPost(Long commentId);

    UpdateCommentResponse updateCommentPost (Long commentId, UpdateCommentRequest request);

    PageResponse<CommentResponse> getCommentByLessonId(Long postId, int page, int size);

    CommentResponse addCommentLesson(CommentRequest request);

    void deleteCommentLesson(Long commentId);

    UpdateCommentResponse updateCommentLesson (Long commentId, UpdateCommentRequest request);
}
