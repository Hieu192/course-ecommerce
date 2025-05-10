package com.Hieu2k3.course.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    String getImage();

    String uploadImage(MultipartFile file);

    String updateAvatar(String url, String email);

    String deleteAvatar();

    Map<String, Object> uploadVideoChunked(MultipartFile file, String folderName) throws IOException;

    File convertMultipartFileToFile(MultipartFile file) throws IOException;
}
