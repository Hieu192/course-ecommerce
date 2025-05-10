package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.requests.user.TeacherRegisterRequest;
import com.Hieu2k3.course.dtos.responses.user.InfoTeacherResponse;
import com.Hieu2k3.course.dtos.responses.user.TeacherResponse;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Review;
import com.Hieu2k3.course.entity.Role;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.enums.RegistrationStatus;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.TeacherMapper;
import com.Hieu2k3.course.repository.CourseRepository;
import com.Hieu2k3.course.repository.RoleRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.TeacherService;
import com.Hieu2k3.course.utils.RoleType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    UserRepository userRepository;
    TeacherMapper teacherMapper;
    RoleRepository roleRepository;
    CourseRepository courseRepository;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<TeacherResponse> getAll() {

        return userRepository.getAllStatusTeacher(RoleType.USER_ROLE, RegistrationStatus.PENDING)
                .stream()
                .map(teacherMapper::toTeacherResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    public TeacherResponse registerTeacher(TeacherRegisterRequest request) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getRegistrationStatus() == null || user.getRegistrationStatus().equals(RegistrationStatus.REJECTED)) {
            user.setRegistrationStatus(RegistrationStatus.PENDING);
            userRepository.save(user);
        }
        return teacherMapper.toTeacherResponse(user);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public TeacherResponse agreeTeacher(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String roleName = user.getRole().getName();
        Role role = roleRepository.findByName(RoleType.TEACHER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if(user.getRegistrationStatus().equals(RegistrationStatus.PENDING)
                && roleName.equals(RoleType.USER_ROLE)) {
            user.setRegistrationStatus(RegistrationStatus.APPROVED);
            user.setRole(role);
            userRepository.save(user);

            return teacherMapper.toTeacherResponse(user);
        } throw new AppException(ErrorCode.REGISTER_TEACHER_INVALID);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public TeacherResponse rejectTeacher(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String roleName = user.getRole().getName();
        Role role = roleRepository.findByName(RoleType.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if(user.getRegistrationStatus().equals(RegistrationStatus.PENDING)
                && roleName.equals(RoleType.USER_ROLE)){
            user.setRegistrationStatus(RegistrationStatus.REJECTED);
            user.setRole(role);
            user.setBio(null);
            user.setCertificate(null);
            user.setCvUrl(null);
            user.setFacebookLink(null);
            userRepository.save(user);
//            List<User> userAdmin = userRepository.findByRoleName(PredefinedRole.ADMIN_ROLE);
//            for (User usersAdmin : userAdmin){
//                notificationService.createNotification(user, usersAdmin, message, title, url);
//            }
        }
        return teacherMapper.toTeacherResponse(user);
    }

    @Override
    public InfoTeacherResponse getInfoTeacherByCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));
        return InfoTeacherResponse.builder()
                .id(course.getId())
                .author(course.getAuthor().getName())
                .rating(getAverageRating(course.getComments()))
                .courseAmount(courseRepository.findByAuthor(course.getAuthor()).size())
                .reviewAmount(course.getComments().size())
                .description(course.getDescription())
                .build();
    }

    public BigDecimal getAverageRating(List<Review> reviews){
        int sum = reviews.stream().mapToInt(Review::getRating).sum();

        return BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
    }
}
