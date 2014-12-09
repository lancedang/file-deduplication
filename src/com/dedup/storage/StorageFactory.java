/**
 * 
 */
package com.dedup.storage;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.windowsazure.services.core.storage.StorageException;

/**
 * @author NTF
 *
 */
public class StorageFactory {

	public static enum StorageType {
		LOCAL, AZURE;
	};

	/**
	 * 
	 */
	public StorageFactory() {
		// TODO Auto-generated constructor stub
	}

	public static IStorage GetStorage(StorageType type, String str)
			throws InvalidKeyException, URISyntaxException, StorageException {
		switch (type) {
		case LOCAL:
			return new LocalStorage(str);

		case AZURE:

			return new AzureStorage(str);
		default:
			break;

		}
		return null;
	}
}
