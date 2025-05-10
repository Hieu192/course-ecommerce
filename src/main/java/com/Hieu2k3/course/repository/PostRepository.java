package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
