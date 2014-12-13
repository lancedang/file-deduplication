/**
 * 
 */
package com.dedup.storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.dedup.Chunk;
import com.dedup.storage.StorageFactory.StorageType;

/**
 * @author NTF
 *
 */
public class LocalStorage implements IStorage {
	private String folder;

	/**
	 * 
	 */
	public LocalStorage(String directory) {
		this.folder = directory;
		// TODO Auto-generated constructor stub
		if (!this.exists(folder)){
			File newFolder = new File(this.folder);
			newFolder.mkdir();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#get(java.lang.String)
	 */
	@Override
	public Chunk get(String fingerprint) throws IOException {
	
		if (this.exists(fingerprint)) {
			// read the object and cast it to chunk
			byte[] data = new byte[(int) new File(folder + fingerprint).length()];
			
			//create stream
			FileInputStream fIn = new FileInputStream(folder + fingerprint);

			try {

				fIn.read(data);
				Chunk niceChunk = new Chunk(data);

				// return the object
				return niceChunk;

			} catch (Exception e) {// this never throw
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				// close streams
				fIn.close();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#put(java.lang.String, com.dedup.Chunk)
	 */
	@Override
	public void put(String fingerprint, Chunk chunk) throws IOException {
		// create streams
		FileOutputStream fOut = new FileOutputStream(folder + fingerprint);
		ByteArrayOutputStream bOs = new ByteArrayOutputStream();

		try {
			// create new file
			File newFile = new File(folder + fingerprint);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}

			// right the chunk data to disk
			bOs.write(chunk.data);
			bOs.writeTo(fOut);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// clean up
			fOut.close();
			bOs.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String fingerprint) {

		return new File(folder + fingerprint).exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#remove(java.lang.String)
	 */
	@Override
	public boolean remove(String fingerprint) {

		File target = new File(folder + fingerprint);
		return target.delete();

	}

	@Override
	public StorageType getType() {
		return StorageFactory.StorageType.LOCAL;
	}

}
