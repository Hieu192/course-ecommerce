package com.Hieu2k3.course.dtos.responses.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {
    Long id;
    String name;
    String avatar;
    String content;
    Integer rating;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    List<ReviewResponse> replies = new ArrayList<>();
}