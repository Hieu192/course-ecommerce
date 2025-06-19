package com.Hieu2k3.course.controllers;


import com.Hieu2k3.course.dtos.requests.post.PostCreationRequest;
import com.Hieu2k3.course.dtos.requests.post.UpdateReactCountRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.post.PostCreationResponse;
import com.Hieu2k3.course.dtos.responses.post.PostResponse;
import com.Hieu2k3.course.entity.Post;
import com.Hieu2k3.course.services.PostService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
@Slf4j
public class PostController {
    PostService postService;

    @PostMapping("/create-post")
    ApiResponse<PostCreationResponse> createPost (@RequestPart("request") @Valid PostCreationRequest request,
                                                  @RequestPart(value = "file", required = false) MultipartFile file) {
        return ApiResponse.<PostCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(postService.createPost(request, file))
                .build();
    }

    @DeleteMapping("/delete-post/{postId}")
    ApiResponse<Void> deletePost (@PathVariable Long postId) {
        postService.deletePost(postId);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete post successfully")
                .build();
    }

    @GetMapping("/get-all-post")
    ApiResponse<PageResponse<PostResponse>> getAllPost (
            @Filter Specification<Post> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size){

        return ApiResponse.<PageResponse<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(postService.getAllPost(spec, page, size))
                .build();
    }

    @GetMapping("/get-post-current-login")
    ApiResponse<PageResponse<PostResponse>> getPostByCurrentLogin(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(postService.getPostByCurrentLogin(page, size))
                .build();
    }

    @PutMapping("/update-like-count")
    ApiResponse<Void> updateLikeCount(@RequestBody UpdateReactCountRequest request) {
        postService.updateLikeCount(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update like successfully")
                .build();
    }
}
