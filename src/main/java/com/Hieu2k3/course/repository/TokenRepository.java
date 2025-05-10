package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.entity.RefreshToken;
import com.Hieu2k3.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
