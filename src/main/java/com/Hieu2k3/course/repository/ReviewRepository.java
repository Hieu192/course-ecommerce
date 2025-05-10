package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.course.id = :courseId")
    List<Review> findByCourseIdAndChapterIsNullAndLessonIsNull(Long courseId);
}
