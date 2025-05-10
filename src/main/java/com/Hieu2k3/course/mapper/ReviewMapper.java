package com.Hieu2k3.course.mapper;

import com.Hieu2k3.course.dtos.responses.review.ReviewResponse;
import com.Hieu2k3.course.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(source = "replies", target = "replies")
    ReviewResponse toReviewResponse(Review review);

}