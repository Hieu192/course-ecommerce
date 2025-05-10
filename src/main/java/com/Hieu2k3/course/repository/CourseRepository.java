package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Course;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findByAuthor(User user);

    @Query("SELECT c.title FROM Course c WHERE c.title LIKE %:query%")
    List<String> findTitleSuggestions(@Param("query") String query);
}
