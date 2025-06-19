package com.Hieu2k3.course.services.impl;


import com.Hieu2k3.course.dtos.requests.favorite.FavoriteRequest;
import com.Hieu2k3.course.dtos.responses.PageResponse;
import com.Hieu2k3.course.dtos.responses.favorite.FavoriteResponse;
import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Favorite;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.mapper.FavoriteMapper;
import com.Hieu2k3.course.repository.CourseRepository;
import com.Hieu2k3.course.repository.FavoriteRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.FavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {

    FavoriteRepository favoriteRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;
    FavoriteMapper favoriteMapper;

    @Override
    @PreAuthorize("isAuthenticated()")
    public void createFavorite(FavoriteRequest request) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSER_NOT_EXISTED));

        boolean isAlreadyFavorite = favoriteRepository.existsByUserAndCourse(user, course);
        if (isAlreadyFavorite) {
            throw new AppException(ErrorCode.ALREADY_IN_FAVORITES);
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .course(course)
                .build();

        favoriteRepository.save(favorite);
    }

    @Override
    public Favorite findById(Integer id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FAVORITE_NOT_EXISTED));
    }

    @Override
    public PageResponse<FavoriteResponse> findAllByUserCurrent(int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Favorite> favorites = favoriteRepository.findByUser(user, pageable);

        return PageResponse.<FavoriteResponse>builder()
                .currentPage(page)
                .pageSize(favorites.getSize())
                .totalPages(favorites.getTotalPages())
                .totalElements(favorites.getTotalElements())
                .data(favorites.getContent().stream().map(favoriteMapper::toFavoriteResponse).toList())
                .build();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void deleteFavorite(Integer favoriteId) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Favorite> favorites = favoriteRepository.findByUser(user);

        Favorite favoriteToDelete = favorites.stream()
                .filter(f -> Objects.equals(f.getId(), favoriteId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favoriteToDelete);
    }
}
