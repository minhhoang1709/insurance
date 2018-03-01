package com.ninelives.insurance.core.provider.storage;

public class StorageException extends Exception {

	private static final long serialVersionUID = -2862515843931228068L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
