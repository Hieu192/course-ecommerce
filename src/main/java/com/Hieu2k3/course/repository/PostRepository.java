package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Post;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Page<Post> findPostByUser(User user, Pageable pageable);
}
