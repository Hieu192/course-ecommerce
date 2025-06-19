package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Favorite;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Page<Favorite> findByUser(User user, Pageable pageable);

    List<Favorite> findByUser(User user);

    boolean existsByUserAndCourse(User user, Course course);
}
