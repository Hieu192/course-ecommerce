package com.Hieu2k3.course.controllers;

import com.Hieu2k3.course.dtos.requests.user.PasswordCreateForFirstRequest;
import com.Hieu2k3.course.dtos.requests.user.TeacherRegisterRequest;
import com.Hieu2k3.course.dtos.requests.user.UserCreateRequest;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.user.TeacherResponse;
import com.Hieu2k3.course.dtos.responses.user.UserResponse;
import com.Hieu2k3.course.services.TeacherService;
import com.Hieu2k3.course.services.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/user")
public class UserController {
    UserService userService;
    TeacherService teacherService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        var result = userService.createUser(request);

        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(result)
                .build();
    }

    @PostMapping("/registerv2")
    public ApiResponse<UserResponse> createUserV2(@RequestBody @Valid UserCreateRequest request,
                                                @RequestParam String otp) {
        var result = userService.createUser(request);

        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(result)
                .build();
    }

    @PostMapping("/create-password")
    ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreateForFirstRequest request){
        userService.createPassword(request);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Password has ben created, you could use it to log-in")
                .build();
    }




    @GetMapping("/registration-teachers")
    ApiResponse<List<TeacherResponse>> getAll(){
        return ApiResponse.<List<TeacherResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(teacherService.getAll())
                .build();
    }

    @PostMapping("/register-teacher")
    ApiResponse<TeacherResponse> registerTeacher(
            @RequestBody TeacherRegisterRequest request
    ) throws IOException, URISyntaxException {

        var result = teacherService.registerTeacher(request);

        return ApiResponse.<TeacherResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/agree-teacher/{id}")
    ApiResponse<TeacherResponse> agreeTeacher (@PathVariable Long id) {
        var result = teacherService.agreeTeacher(id);

        return ApiResponse.<TeacherResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Approve Teacher Successfully")
                .result(result)
                .build();
    }

    @PostMapping("/reject-teacher/{id}")
    ApiResponse<TeacherResponse> rejectTeacher(@PathVariable Long id) {
        var result = teacherService.rejectTeacher(id);

        return ApiResponse.<TeacherResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Reject Successfully")
                .result(result)
                .build();
    }
}
