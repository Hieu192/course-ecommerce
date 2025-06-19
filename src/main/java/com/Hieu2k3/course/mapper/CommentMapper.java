package com.Hieu2k3.course.mapper;

import com.Hieu2k3.course.dtos.requests.comment.CommentRequest;
import com.Hieu2k3.course.dtos.requests.comment.UpdateCommentRequest;
import com.Hieu2k3.course.dtos.responses.comment.CommentResponse;
import com.Hieu2k3.course.entity.Comment;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface CommemtMapper {

    Comment toComment (CommentRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "avatar", source = "user.avatar")
    @Mapping(target = "replies", source = "replies")
    CommentResponse toCommentResponse (Comment comment);

    void updateComment(UpdateCommentRequest request, @MappingTarget Comment comment);
}
