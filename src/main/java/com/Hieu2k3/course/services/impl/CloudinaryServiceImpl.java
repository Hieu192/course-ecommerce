package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.entity.User;
import com.Hieu2k3.course.exception.AppException;
import com.Hieu2k3.course.exception.ErrorCode;
import com.Hieu2k3.course.repository.UserRepository;
import com.Hieu2k3.course.security.SecurityUtils;
import com.Hieu2k3.course.services.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    Cloudinary cloudinary;
    UserRepository userRepository;

    @Override
    public String getImage() {
        String currentLogin = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(currentLogin)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return (user.getAvatar() != null) ? user.getAvatar() : "";
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public String uploadImage(MultipartFile file) {
        try {
            var result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "/upload",
                    "use_filename", true,
                    "unique_filename", true,
                    "resource_type","auto"
            ));
            return  result.get("secure_url").toString();
        } catch (IOException io) {
            throw new RuntimeException("upload failed");
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public String updateAvatar(String url, String email) {
        String currentUserEmail = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setAvatar(url);
        userRepository.save(user);

        log.info("Avatar updated successfully for user with email: {}", email);
        return "Avatar updated successfully for user with email:";
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public String deleteAvatar() {
        String currentUserEmail = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setAvatar(null);
        userRepository.save(user);

        log.info("Avatar deleted successfully for user with email: {}", currentUserEmail);
        return "Avatar deleted successfully for user with email: {}";
    }

    @Override
    public Map<String, Object> uploadVideoChunked(MultipartFile file, String folderName) throws IOException {
        File tempFile = convertMultipartFileToFile(file); // xử lý để Cloudinary SDK có thể xử lý được
        // upload file theo từng phần
        Map<String, Object> uploadResult = cloudinary.uploader().uploadLarge(tempFile, ObjectUtils.asMap(
                "resource_type", "video",
                "folder", folderName,
                "chunk_size", 6000000
        ));
        tempFile.delete(); // Xóa file tạm sau khi tải lên
        return uploadResult;
    }

    // Tạo file tạm trong thư mục
    @Override
    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }
}
