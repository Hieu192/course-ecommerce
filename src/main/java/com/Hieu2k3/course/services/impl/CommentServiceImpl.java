package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.comment.CommentRequest;
import com.Hieu2k3.course.dtos.requests.comment.UpdateCommentRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.comment.CommentResponse;
import com.Hieu2k3.course.dtos.responses.comment.UpdateCommentResponse;
import com.Hieu2k3.course.entity.Comment;
import com.Hieu2k3.course.entity.Post;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.CommentMapper;
import com.Hieu2k3.course.repository.CommentRepository;
import com.Hieu2k3.course.repository.PostRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.CommentService;
import com.Hieu2k3.course.utils.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    @Override
    @PreAuthorize("isAuthenticated()")
    public PageResponse<CommentResponse> getCommentByPostId(Long postId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Comment> parentComments = commentRepository.findCommentByPostIdAndParentCommentIsNull(postId, pageable);

        List<CommentResponse> responses = parentComments.getContent()
                .stream()
                .map(comment -> {
                    CommentResponse response = commentMapper.toCommentResponse(comment);

                    List<CommentResponse> replies = comment.getReplies().stream()
                            .map(commentMapper::toCommentResponse)
                            .toList();
                    response.setReplies(replies);
                    return response;
                })
                .toList();

        return PageResponse.<CommentResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(parentComments.getTotalElements())
                .totalPages(parentComments.getTotalPages())
                .data(responses)
                .build();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public CommentResponse addCommentPost(CommentRequest request) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_ID_INVALID));

        Comment parentComment = null;
        if(request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new AppException(ErrorCode.PARENT_COMMENT_NOT_EXISTED));
        }

        if ((request.getContent() == null || request.getContent().isEmpty())) {
            throw new AppException(ErrorCode.CONTENT_COMMENT_INVALID);
        }

//        if (bannedWordsService.containsBannedWords(request.getContent())) {
//            throw new AppException(ErrorCode.INVALID_COMMENT_CONTENT);
//        }

        Comment comment = commentMapper.toComment(request);
        comment.setUser(user);
        comment.setPost(post);
        comment.setParentComment(parentComment);

        commentRepository.save(comment);

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void deleteCommentPost(Long commentId) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));

        if (Objects.equals(user.getId(), comment.getUser().getId()) ||
                RoleType.ADMIN_ROLE.equals(user.getRole().getName())) {
            commentRepository.delete(comment);
            return;
        }

        throw new AppException(ErrorCode.DELETE_COMMENT_INVALID);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public UpdateCommentResponse updateCommentPost(Long commentId, UpdateCommentRequest request) {

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));

        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new AppException(ErrorCode.UPDATE_COMMENT_INVALID);
        }

        commentMapper.updateComment(request, comment);
        commentRepository.save(comment);

        return UpdateCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public PageResponse<CommentResponse> getCommentByLessonId(Long postId, int page, int size) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public CommentResponse addCommentLesson(CommentRequest request) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void deleteCommentLesson(Long commentId) {

    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public UpdateCommentResponse updateCommentLesson(Long commentId, UpdateCommentRequest request) {
        return null;
    }
}
