package com.ninelives.insurance.core.jackson.transformer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.model.UserFile;

public class UserFileToBase64JsonSerializer extends JsonSerializer<UserFile>{
	private static final Logger logger = LoggerFactory.getLogger(UserFileToBase64JsonSerializer.class);
			
	StorageProvider storageProvider;
	
	public UserFileToBase64JsonSerializer(StorageProvider storageProvider) {
		this.storageProvider = storageProvider;		
	}
	
	@Override
	public void serialize(UserFile userFile, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		try {
			Resource resource = storageProvider.loadAsResource(userFile);
			gen.writeBinary(resource.getInputStream(), -1);			
		} catch (StorageException e) {
			logger.error("Exception on fetching user file", e);
			throw new IOException(e.getMessage());
		}
	}

}
