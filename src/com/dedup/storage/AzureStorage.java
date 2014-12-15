/**
 * 
 */
package com.dedup.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.dedup.Chunk;
import com.dedup.storage.StorageFactory.StorageType;
import com.microsoft.windowsazure.services.core.storage.*;
import com.microsoft.windowsazure.services.blob.client.*;

/**
 * @author NTF
 *
 */
public class AzureStorage implements IStorage {

	private CloudStorageAccount storageAccount;
	private CloudBlobClient client;
	private CloudBlobContainer container;

	/**
	 * @param object
	 * @throws URISyntaxException
	 * @throws InvalidKeyException
	 * @throws StorageException
	 * 
	 */
	public AzureStorage(String connectionString) throws InvalidKeyException,
			URISyntaxException, StorageException {
		// System.out.println(connectionString);
		storageAccount = CloudStorageAccount.parse(connectionString);
		client = this.storageAccount.createCloudBlobClient();
		container = this.client.getContainerReference("csci4180deduplication");
		container.createIfNotExist();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#get(java.lang.String)
	 */
	@Override
	public Chunk get(String fingerprint) throws URISyntaxException,
			StorageException, IOException {

		CloudBlockBlob blob = container.getBlockBlobReference("blocks/"
				+ fingerprint);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		blob.download(stream);

		return new Chunk(stream.toByteArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#put(java.lang.String, com.dedup.Chunk)
	 */
	@Override
	public void put(String fingerprint, Chunk chunk) throws StorageException,
			IOException, URISyntaxException {
		CloudBlockBlob blob = container.getBlockBlobReference("blocks/"
				+ fingerprint);

		blob.upload(new ByteArrayInputStream(chunk.data), chunk.data.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String fingerprint) throws StorageException,
			URISyntaxException {
		// TODO Auto-generated method stub
		CloudBlockBlob blob = container.getBlockBlobReference("blocks/"
				+ fingerprint);
		return blob.exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dedup.storage.IStorage#remove(java.lang.String)
	 */
	@Override
	public boolean remove(String fingerprint) throws URISyntaxException,
			StorageException {
		CloudBlockBlob blob = container.getBlockBlobReference("blocks/"
				+ fingerprint);
		return blob.deleteIfExists();
	}

	@Override
	public StorageType getType() {
		return StorageFactory.StorageType.AZURE;
	}

	@Override
	public long length(String fingerprint) throws URISyntaxException,
			StorageException {
		CloudBlockBlob blob = container.getBlockBlobReference("blocks/"
				+ fingerprint);
		blob.downloadAttributes();
		BlobProperties x = blob.getProperties();

		return x.getLength();
	}

}
