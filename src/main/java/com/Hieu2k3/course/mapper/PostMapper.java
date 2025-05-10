package com.Hieu2k3.course.mapper;

import com.Hieu2k3.course.dtos.requests.post.PostCreationRequest;
import com.Hieu2k3.course.dtos.responses.post.PostCreationResponse;
import com.Hieu2k3.course.dtos.responses.post.PostResponse;
import com.Hieu2k3.course.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostCreationRequest request);

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "avatar", source = "user.avatar")
    PostCreationResponse toPostCreationResponse(Post post);

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "avatar", source = "user.avatar")
    @Mapping(target = "likeCount", source = "likeCount")
    PostResponse toPostResponse(Post post);
}