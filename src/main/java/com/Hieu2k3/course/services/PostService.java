package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.post.PostCreationRequest;
import com.Hieu2k3.course.dtos.requests.post.UpdateReactCountRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.post.PostCreationResponse;
import com.Hieu2k3.course.dtos.responses.post.PostResponse;
import com.Hieu2k3.course.entity.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostCreationResponse createPost (PostCreationRequest request, MultipartFile file);

    PageResponse<PostResponse> getAllPost (Specification<Post> spec, int page, int size);

    PageResponse<PostResponse> getPostByCurrentLogin (int page, int size);

    void deletePost (Long postId);

    void updateLikeCount(UpdateReactCountRequest request);
}
