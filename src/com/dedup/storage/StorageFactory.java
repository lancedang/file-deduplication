/**
 * 
 */
package com.dedup.storage;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.HashMap;

import com.microsoft.windowsazure.services.core.storage.StorageException;

/**
 * @author NTF
 *
 */
public class StorageFactory {

	public static enum StorageType {
		LOCAL, AZURE;
	};

	protected static HashMap<StorageType, IStorage> storages = new HashMap<StorageType, IStorage>();

	/**
	 * 
	 */
	public StorageFactory() {
		// TODO Auto-generated constructor stub
	}

	public static IStorage createStorage(StorageType type, String str)
			throws InvalidKeyException, URISyntaxException, StorageException {
		IStorage storage = null;
		switch (type) {
		case LOCAL:
			storage = new LocalStorage(str);
			break;
		case AZURE:
			storage = new AzureStorage(str);
			break;
		default:
			throw new InvalidKeyException("unknown StorageType");
		}

		StorageFactory.storages.put(type, storage);
		return storage;

	}

	public static IStorage getStorage(StorageType type)
			throws InvalidKeyException, URISyntaxException, StorageException {
		return StorageFactory.storages.containsKey(type) ? StorageFactory.storages
				.get(type) : null;
	}
}
