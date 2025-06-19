package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.Enrollment;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserAndCourse(User user, Course course);
    List<Enrollment> findCourseByUser(User user);
}
