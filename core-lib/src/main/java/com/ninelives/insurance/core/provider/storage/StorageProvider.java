package com.ninelives.insurance.core.provider.storage;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.model.UserFile;

public interface StorageProvider {

    void init() throws StorageException;

    void store(MultipartFile file, UserFile userFile) throws StorageException;
    
    void move(UserFile userFileSrc, UserFile userFileDst) throws StorageException;
    
    Path prepare(String filename) throws StorageException;

    //Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(UserFile userFile) throws StorageException;
    
    Resource loadAsResource(String filename) throws StorageException;

    //void deleteAll();

}
