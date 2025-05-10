package com.Hieu2k3.course.dtos.requests.post;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReactCountRequest {
    Long postId;
    Boolean isLike;
}
