package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Chapter;
import com.Hieu2k3.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Optional<Chapter> findByChapterNameAndCourse(String chapterName, Course course);

    List<Chapter> findByCourseId(Long courseId);
}
