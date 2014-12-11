/**
 * 
 */
package com.dedup.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.dedup.Chunk;
import com.dedup.storage.StorageFactory.StorageType;

/**
 * @author NTF
 *
 */
public class LocalStorage implements IStorage {
	private static final String folder = "data/";
	/**
	 * 
	 */
	public LocalStorage(String directory) {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#get(java.lang.String)
	 */
	@Override
	public Chunk get(String fingerprint) {
		
		if (this.exists(fingerprint)){
			try {
				//read object from data/
				FileInputStream fIn = new FileInputStream(folder + fingerprint);
				ObjectInputStream ois = new ObjectInputStream(fIn);
				
				//read the object and cast it to chunk
				Chunk niceChunk = (Chunk)ois.readObject();
				
				//close streams
				ois.close();
				fIn.close();
				
				//return the object
				return niceChunk;
				
			} catch (Exception e) {//this never throw
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public void put(String fingerprint, Chunk chunk) {
		
		try {
                        //create new file
                        File newFile = new File(folder + fingerprint);
                        if(!newFile.exists()){
                            newFile.createNewFile();
                        }
                        
			//create streams
			FileOutputStream fOut = new FileOutputStream(folder + fingerprint);
			ObjectOutputStream oos = new ObjectOutputStream(fOut);
			
			//right the object to disk
			oos.writeObject(chunk);
			
			//clean up
			oos.close();
			fOut.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
