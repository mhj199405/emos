package com.tongtech.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.tongtech.common.StorageException;
import com.tongtech.common.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;
    private final Path locationDownload;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.locationDownload = Paths.get(properties.getLocationDownload());
    }

    @Override
    public void init() {



        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(locationDownload);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
    @Override
    public void delete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new StorageException("Could not delete " + path.toString(), e);
        }
    }

    @Override
    public void deleteRecursively(String pre) {
        String			prename  = StringUtils.cleanPath(pre);
        StringBuffer	sb = new StringBuffer();
        Path			tmp;

        tmp = Paths.get(prename);
        tmp.forEach(name->sb.append(name).append("/"));
        sb.deleteCharAt(sb.length()-1);
        prename = sb.toString();

        Path path = this.rootLocation.resolve(prename);
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public Path store(MultipartFile file) {
        return store("", file);
    }

    @Override
    public Path store(String pre, MultipartFile file) {
        String			prename  = StringUtils.cleanPath(pre);
        String			filename = StringUtils.cleanPath(file.getOriginalFilename());
        StringBuffer	sb = new StringBuffer();
        Path			tmp;
        int				index;
        String			dirname;

        tmp = Paths.get(prename);
        tmp.forEach(name->sb.append(name).append("/"));
        tmp = Paths.get(filename);
        tmp.forEach(name->sb.append(name).append("/"));
        sb.deleteCharAt(sb.length()-1);
        filename = sb.toString();
        dirname = filename;
        index = dirname.lastIndexOf("/");
        if (index != -1)
            dirname = dirname.substring(0, index);


        Path path = null;
        Path dir  = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            dir  = this.rootLocation.resolve(dirname);
            Files.createDirectories(dir);
            path = this.rootLocation.resolve(filename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return path;
    }

    @Override
    public Path load(String i_filename) {
        String filename = checkFilename(i_filename);
        return locationDownload.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String i_filename) {
        String filename = checkFilename(i_filename);

        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageException("Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    private String checkFilename(String i_filename) {
        Path			tmp       = Paths.get(i_filename);
        StringBuffer	sb        = new StringBuffer();
        String			filename  = null;

        tmp.forEach(name->sb.append(name).append("/"));
        sb.deleteCharAt(sb.length()-1);
        filename = sb.toString();

        if (filename.contains("..")) {
            // This is a security check
            throw new StorageException(
                    "Cannot store file with relative path outside current directory "
                            + filename);
        }

        return filename;
    }
}
