package com.Hieu2k3.course.controllers;


import com.Hieu2k3.course.dtos.requests.comment.CommentRequest;
import com.Hieu2k3.course.dtos.requests.comment.UpdateCommentRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.comment.CommentResponse;
import com.Hieu2k3.course.dtos.responses.comment.UpdateCommentResponse;
import com.Hieu2k3.course.services.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/comment")
@Slf4j
public class CommentController {

    CommentService commentService;

    @GetMapping("/post-comment/{postId}")
    ApiResponse<PageResponse<CommentResponse>> findAll(
            @PathVariable Long postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {

        return ApiResponse.<PageResponse<CommentResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(commentService.getCommentByPostId(postId, page, size))
                .build();
    }

    @PostMapping("/post")
    ApiResponse<CommentResponse> addComment (@RequestBody @Valid CommentRequest request){
        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(commentService.addCommentPost(request))
                .build();
    }

    @DeleteMapping("/post/{commentId}")
    ApiResponse<Void> deleteComment (@PathVariable Long commentId){
        commentService.deleteCommentPost(commentId);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Comment Successfully")
                .build();
    }

    @PutMapping("/post/{commentId}")
    ApiResponse<UpdateCommentResponse> updateComment (@PathVariable Long commentId, @RequestBody @Valid UpdateCommentRequest request) {
        return ApiResponse.<UpdateCommentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update Comment Successfully")
                .result(commentService.updateCommentPost(commentId, request))
                .build();
    }
}
