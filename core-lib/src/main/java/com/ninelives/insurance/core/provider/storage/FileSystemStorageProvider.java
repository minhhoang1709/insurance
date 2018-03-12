package com.ninelives.insurance.core.provider.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.model.UserFile;

@Service
public class FileSystemStorageProvider implements StorageProvider {
	private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageProvider.class);
	
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageProvider(NinelivesConfigProperties config) {
        this.rootLocation = Paths.get(config.getStorage().getLocation());
    }

    @Override
    public void store(MultipartFile file, UserFile userFile) throws StorageException {
        String filepath = StringUtils.cleanPath(userFile.getFilePath());
        
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filepath);
            }
            if (filepath.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filepath);
            }
        	Files.createDirectories(this.rootLocation.resolve(filepath));        	

            Files.copy(file.getInputStream(), this.rootLocation.resolve(filepath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filepath, e);
        }
    }

	@Override
	public void move(UserFile userFileSrc, UserFile userFileDst) throws StorageException {
		String destPath = StringUtils.cleanPath(userFileDst.getFilePath());
		try {
        	Files.createDirectories(this.rootLocation.resolve(destPath));
        	
			Files.move(this.rootLocation.resolve(userFileSrc.getFilePath()), this.rootLocation.resolve(destPath),
					StandardCopyOption.REPLACE_EXISTING);

        }
        catch (IOException e) {
        	logger.error("Error on move file ", e);
            throw new StorageException("Failed to move file " + destPath +" with message "+e.getMessage(), e);
        }		
	}
//    @Override
//    public Stream<Path> loadAll() {
//        try {
//            return Files.walk(this.rootLocation, 1)
//                    .filter(path -> !path.equals(this.rootLocation))
//                    .map(path -> this.rootLocation.relativize(path));
//        }
//        catch (IOException e) {
//            throw new StorageException("Failed to read stored files", e);
//        }
//
//    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(UserFile userFile) throws StorageException {
    	return loadAsResource(userFile.getFilePath());
    }
    
    @Override
    public Resource loadAsResource(String filename) throws StorageException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

//    @Override
//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(rootLocation.toFile());
//    }

    @Override
    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

}