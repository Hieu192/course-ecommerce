package com.Hieu2k3.course.dtos.requests.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCommentRequest {

    @NotBlank(message = "CONTENT_COMMENT_INVALID")
    @Size( max = 500, message = "CONTENT_INVALID")
    String content;
}