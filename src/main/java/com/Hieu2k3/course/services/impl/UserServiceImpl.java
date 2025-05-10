package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.user.PasswordCreateForFirstRequest;
import com.Hieu2k3.course.dtos.requests.user.UserCreateRequest;
import com.Hieu2k3.course.dtos.responses.user.UserResponse;
import com.Hieu2k3.course.entity.Role;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.UserMapper;
import com.Hieu2k3.course.repository.RoleRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.UserService;
import com.Hieu2k3.course.utils.RoleType;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userCreateRequest);

        Role role = roleRepository.findByName(RoleType.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Created role: {}", role);
        user.setRole(role);
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());
        user.setName(userCreateRequest.getFirstName() + " " + userCreateRequest.getLastName());
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    public void createPassword(PasswordCreateForFirstRequest request) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (StringUtils.hasText(user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public Page<User> findAllUsers(String keyword, Pageable pageable) {
        return null;
//        return userRepository.findAll(keyword, pageable);
    }

    @Override
    public UserResponse getMyInfo() {
        return null;
    }

    @Override
    public void sendOtpForgotPassword() {

    }

    @Override
    public void sendOtpRegister() {

    }

    @Override
    public void resetPassword() {

    }

    @Override
    public String generateOtp() {
        return "";
    }

}
