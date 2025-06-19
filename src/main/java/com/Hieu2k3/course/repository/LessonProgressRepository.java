package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Lesson;
import com.Hieu2k3.course.entity.LessonProgress;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.user = :user AND lp.lesson.chapter.course = :course AND lp.completed = :completed")
    long countByUserAndCourseAndCompleted(@Param("user") User user, @Param("course") Course course, @Param("completed") Boolean completed);

    @Query("SELECT lp FROM LessonProgress lp WHERE lp.user = :user AND lp.lesson.chapter.course = :course AND lp.completed = :completed")
    List<LessonProgress> findByUserAndCourse(@Param("user") User user, @Param("course") Course course, @Param("completed") Boolean completed);

    LessonProgress findByUserAndLesson(User user, Lesson lesson);

    @Query("select count(lp) from LessonProgress lp where lp.user=:user and lp.lesson.chapter.course=:course")
    long totalLessonComplete(User user, Course course);
}
