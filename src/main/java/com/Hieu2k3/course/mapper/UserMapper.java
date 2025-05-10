package com.Hieu2k3.course.mapper;

import com.Hieu2k3.course.dtos.requests.user.UserCreateRequest;
import com.Hieu2k3.course.dtos.responses.user.InfoTeacherResponse;
import com.Hieu2k3.course.dtos.responses.user.UserResponse;
import com.Hieu2k3.course.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest request);

    @Mapping(source = "role.name", target = "role") // ánh xạ riêng name của role
    UserResponse toUserResponse(User user);
}