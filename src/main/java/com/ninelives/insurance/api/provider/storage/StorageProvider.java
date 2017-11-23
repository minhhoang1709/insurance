package com.ninelives.insurance.api.provider.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.model.UserFile;

public interface StorageProvider {

    void init();

    void store(MultipartFile file, UserFile userFile);

    //Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    //void deleteAll();

}
