package com.Hieu2k3.course.dtos.requests.post;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {

    @NotBlank(message = "CONTENT_POST_INVALID")
    String content;

    String image;

}