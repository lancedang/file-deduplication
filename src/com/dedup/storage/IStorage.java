/**
 * 
 */
package com.dedup.storage;

import java.io.IOException;
import java.net.URISyntaxException;

import com.dedup.Chunk;
import com.microsoft.windowsazure.services.core.storage.StorageException;

/**
 * @author NTF
 *
 */
public interface IStorage {

	StorageFactory.StorageType getType();
	/**
	 * get a Chunk from storage
	 * 
	 * @param fingerprint
	 * @return
	 * @throws StorageException 
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	Chunk get(String fingerprint) throws URISyntaxException, StorageException, IOException;

	/**
	 * put a Chunk to storage
	 * 
	 * @param fingerprint
	 * @param chunk
	 * @throws IOException 
	 * @throws StorageException 
	 * @throws URISyntaxException 
	 */
	void put(String fingerprint, Chunk chunk) throws StorageException, IOException, URISyntaxException;

	/**
	 * check if the chunk with this fingerprint exists
	 * 
	 * @param fingerprint
	 * @return
	 * @throws StorageException 
	 * @throws URISyntaxException 
	 */
	boolean exists(String fingerprint) throws StorageException, URISyntaxException;

	/**
	 * 
	 * @param fingerprint
	 * @return success or not
	 * @throws StorageException 
	 * @throws URISyntaxException 
	 */
	boolean remove(String fingerprint) throws URISyntaxException, StorageException;
}
