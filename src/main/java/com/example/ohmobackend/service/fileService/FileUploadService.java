package com.example.ohmobackend.service.fileService;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String upload(MultipartFile file, String fileName);
    void delete(String fileName);
}