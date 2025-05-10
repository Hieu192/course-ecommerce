package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.requests.user.PasswordCreateForFirstRequest;
import com.Hieu2k3.course.dtos.requests.user.UserCreateRequest;
import com.Hieu2k3.course.dtos.responses.user.UserResponse;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest userCreateRequest);

    void createPassword(PasswordCreateForFirstRequest request);

    List<UserResponse> getAllUsers();

    Page<User> findAllUsers(String keyword, Pageable pageable);

    UserResponse getMyInfo();

    void sendOtpForgotPassword();

    void sendOtpRegister();

    void resetPassword();

    String generateOtp();
}
