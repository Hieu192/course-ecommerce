package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.comment.CommentRequest;
import com.Hieu2k3.course.dtos.requests.comment.UpdateCommentRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.comment.CommentResponse;
import com.Hieu2k3.course.dtos.responses.comment.UpdateCommentResponse;
import com.Hieu2k3.course.services.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;

public class CommentServiceImpl implements CommentService {

    @Override
//    @PreAuthorize("isAuthenticated()")
    public PageResponse<CommentResponse> getCommentByPostId(Long postId, int page, int size) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public CommentResponse addCommentPost(CommentRequest request) {
        return null;
    }

    @Override
    public void deleteCommentPost(Long commentId) {

    }

    @Override
    public UpdateCommentResponse updateCommentPost(Long commentId, UpdateCommentRequest request) {
        return null;
    }

    @Override
    public PageResponse<CommentResponse> getCommentByLessonId(Long postId, int page, int size) {
        return null;
    }

    @Override
    public CommentResponse addCommentLesson(CommentRequest request) {
        return null;
    }

    @Override
    public void deleteCommentLesson(Long commentId) {

    }

    @Override
    public UpdateCommentResponse updateCommentLesson(Long commentId, UpdateCommentRequest request) {
        return null;
    }
}
