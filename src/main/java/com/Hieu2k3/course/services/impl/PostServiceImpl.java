package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.post.PostCreationRequest;
import com.Hieu2k3.course.dtos.requests.post.UpdateReactCountRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.post.PostCreationResponse;
import com.Hieu2k3.course.dtos.responses.post.PostResponse;
import com.Hieu2k3.course.entity.Post;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.PostMapper;
import com.Hieu2k3.course.repository.PostRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.CloudinaryService;
import com.Hieu2k3.course.services.PostService;
import com.Hieu2k3.course.utils.RoleType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostServiceImpl implements PostService {

    UserRepository userRepository;
    PostRepository postRepository;
    CloudinaryService cloudinaryService;
    PostMapper postMapper;

    @Override
    @PreAuthorize("isAuthenticated()")
    public PostCreationResponse createPost(PostCreationRequest request, MultipartFile file) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(file != null){
            String image = cloudinaryService.uploadImage(file);
            request.setImage(image);
        }
        Post post = postMapper.toPost(request);
        post.setUser(user);

        postRepository.save(post);

        return postMapper.toPostCreationResponse(post);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public PageResponse<PostResponse> getAllPost(Specification<Post> spec, int page, int size) {
        return null;
    }


    @PreAuthorize("isAuthenticated()")
    public PageResponse<PostResponse> getAllPostV2(Specification<Post> spec, int page, int size) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public PageResponse<PostResponse> getPostByCurrentLogin(int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return null;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void deletePost(Long postId) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_ID_INVALID));

        if (!Objects.equals(user.getId(), post.getUser().getId()) &&
                !Objects.equals(user.getRole().getName(), RoleType.ADMIN_ROLE)) {
            throw new AppException(ErrorCode.DELETE_POST_INVALID);
        }
        postRepository.delete(post);

    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void updateLikeCount(UpdateReactCountRequest request) {

    }
}
