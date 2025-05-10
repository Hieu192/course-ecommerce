package com.Hieu2k3.course.config;


import com.Hieu2k3.course.entity.Role;
import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.repository.RoleRepository;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.utils.RoleType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfiguration {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin@gmail.com";
    @NonFinal
    static final String ADMIN_PASSWORD = "123456";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver" // chỉ chạy khi dùng mysql
    )
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application.....");

        // tạo 3 Role mặc đinhj USER, TEACHER, ADMIN
        return args -> {
            Optional<Role> userRole = roleRepository.findByName(RoleType.USER_ROLE);
            if (userRole.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(RoleType.USER_ROLE)
                        .description("User role")
                        .build());
            }

            Optional<Role> adminRole = roleRepository.findByName(RoleType.ADMIN_ROLE);
            if (adminRole.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(RoleType.ADMIN_ROLE)
                        .description("Admin role")
                        .build());
            }

            Optional<Role> teacherRole = roleRepository.findByName(RoleType.TEACHER_ROLE);
            if(teacherRole.isEmpty()){
                roleRepository.save(Role.builder()
                        .name(RoleType.TEACHER_ROLE)
                        .description("Teacher role")
                        .build());
            }

            if (userRepository.findByEmail(ADMIN_USER_NAME).isEmpty()) {
                Role roleADM = roleRepository.findByName(RoleType.ADMIN_ROLE)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

                User user = User.builder()
                        .email(ADMIN_USER_NAME)
                        .firstName("Pham Quang")
                        .lastName("Hieu")
                        .name(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role(roleADM)
                        .dob(LocalDate.of(2003, 2, 19))
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default password: 123456, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
