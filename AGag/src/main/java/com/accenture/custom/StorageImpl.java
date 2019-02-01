package com.accenture.custom;

import com.accenture.exceptions.StorageException;
import com.accenture.exceptions.StorageFileNotFoundException;
import com.accenture.utils.Util;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StorageImpl implements Storage {
    private Path rootLocation = Paths.get(Util.DEFAULT_UPLOAD_DIR);

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
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
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String storeWithCustomLocation(String location, MultipartFile file, String oldPath) {

        setUploadLocation(location);
        File f = new File(rootLocation + "/" + oldPath);
        if (f.exists()) {
            f.delete();
        }
        store(file);
        restartUploadLocation();

        return "/" + Util.DEFAULT_UPLOAD_DIR + "/" + location + "/" + file.getOriginalFilename();

    }

    @Override
    public String storeWithCustomLocation(String location, MultipartFile file) {

        setUploadLocation(location);
        store(file);
        restartUploadLocation();
        return "/" + Util.DEFAULT_UPLOAD_DIR + "/" + location + "/" + file.getOriginalFilename();

    }


    private void setUploadLocation(String location) {
        rootLocation = Paths.get(Util.DEFAULT_UPLOAD_DIR + "/" + location);
        init();
    }

    private void restartUploadLocation() {
        rootLocation = Paths.get(Util.DEFAULT_UPLOAD_DIR);
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
