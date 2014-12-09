/**
 * 
 */
package com.dedup.storage;

import com.dedup.Chunk;

/**
 * @author NTF
 *
 */
public class LocalStorage implements IStorage {

	/**
	 * 
	 */
	public LocalStorage(String directory) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dedup.storage.IStorage#get(java.lang.String)
	 */
	@Override
	public Chunk get(String fingerprint) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dedup.storage.IStorage#put(java.lang.String, com.dedup.Chunk)
	 */
	@Override
	public void put(String fingerprint, Chunk chunk) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.dedup.storage.IStorage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String fingerprint) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.dedup.storage.IStorage#remove(java.lang.String)
	 */
	@Override
	public boolean remove(String fingerprint) {
		// TODO Auto-generated method stub
		return false;
	}

}
