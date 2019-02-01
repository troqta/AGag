package com.accenture.custom;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface Storage {


    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    String storeWithCustomLocation(String location, MultipartFile file, String oldPath);

    String storeWithCustomLocation(String location, MultipartFile file);


}
