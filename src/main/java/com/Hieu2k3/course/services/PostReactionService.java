package com.Hieu2k3.course.services;

import com.Hieu2k3.course.enums.ReactionType;

import java.util.Map;

public interface PostReactionService {
    void reactToPost(Long postId, Long userId, ReactionType reactionType);

    Map<ReactionType, Long> getReactionStats(Long postId);

}
