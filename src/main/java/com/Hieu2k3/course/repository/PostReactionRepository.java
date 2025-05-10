package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.Post;
import com.Hieu2k3.course.entity.PostReaction;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction>  findByUserAndPost(User user, Post post);

    @Query("SELECT r.reactionType, COUNT(r) FROM PostReaction r WHERE r.post.id = :postId GROUP BY r.reactionType")
    List<Object[]> countReactionsByReactionType(@Param("postId") Long postId);

}
