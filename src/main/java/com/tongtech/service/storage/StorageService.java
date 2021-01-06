package com.tongtech.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();
    Path store(MultipartFile file);
    Path store(String pre, MultipartFile file);
    Path load(String filename);
    Resource loadAsResource(String filename);
    void delete(Path path);
    void deleteRecursively(String pre);
}
