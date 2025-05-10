package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.entity.Post;
import com.Hieu2k3.course.entity.PostReaction;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.enums.ReactionType;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.repository.PostReactionRepository;
import com.Hieu2k3.course.repository.PostRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.PostReactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostReactionServiceImpl implements PostReactionService {

    UserRepository userRepository;
    PostRepository postRepository;
    PostReactionRepository postReactionRepository;

    @Override
    @PreAuthorize("isAuthenticated()")
    public void reactToPost(Long postId, Long userId, ReactionType reactionType) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_ID_INVALID));

        Optional<PostReaction> existingReactionOpt = postReactionRepository.findByUserAndPost(user, post);

        if (existingReactionOpt.isEmpty()) {
            // Trường hợp 1: Chưa từng thả
            PostReaction newReaction = PostReaction.builder()
                    .user(user)
                    .post(post)
                    .reactionType(reactionType)
                    .build();
            postReactionRepository.save(newReaction);
            post.setLikeCount(post.getLikeCount() + 1);

        } else {
            PostReaction existingReaction = existingReactionOpt.get();

            if (existingReaction.getReactionType() == reactionType) {
                // Trường hợp 2: Thả cùng loại → bỏ thả
                postReactionRepository.delete(existingReaction);
                post.setLikeCount(post.getLikeCount() - 1);

            } else {
                // Trường hợp 3: Thả loại khác → cập nhật
                existingReaction.setReactionType(reactionType);
                postReactionRepository.save(existingReaction);
            }
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Map<ReactionType, Long> getReactionStats(Long postId) {
        List<Object[]> stats = postReactionRepository.countReactionsByReactionType(postId);
        Map<ReactionType, Long> result = new HashMap<>();

        for (Object[] row : stats) {
            ReactionType type = (ReactionType) row[0];
            Long count = (Long) row[1];
            result.put(type, count);
        }

        return result;
    }
}
