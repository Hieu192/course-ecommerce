package com.Hieu2k3.course.repository;

import com.Hieu2k3.course.dtos.responses.user.TeacherResponse;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.enums.RegistrationStatus;
import com.Hieu2k3.course.utils.ConfixSql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role.name = :role AND u.registrationStatus = :status")
    List<User> getAllStatusTeacher(@Param("role") String role, @Param("status") RegistrationStatus status);

    // lấy ra tất cả user (ngoại trừ admin) với truyền admin
//    @Query(ConfixSql.User.GET_ALL_USER)
//    Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);
}
