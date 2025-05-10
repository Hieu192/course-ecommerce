package com.Hieu2k3.course.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int code = 200;
    private String message;
    private T result;

    private boolean success = false;
    private List<String> errors;
    private String error;
}
