package com.ninelives.insurance.core.provider.storage;

public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = 6687212914924508475L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}