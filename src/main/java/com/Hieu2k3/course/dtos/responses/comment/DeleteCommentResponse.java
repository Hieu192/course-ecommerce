package com.Hieu2k3.course.dtos.responses.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteCommentResponse {

    Long id;
    String message;
}