package com.Hieu2k3.course.mapper;


import com.Hieu2k3.course.dtos.responses.user.TeacherResponse;
import com.Hieu2k3.course.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "id", source = "id") // Thêm ánh xạ cho trường id
    TeacherResponse toTeacherResponse(User user);


}
