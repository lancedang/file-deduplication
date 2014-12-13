/**
 * 
 */
package com.dedup;

import java.io.Serializable;

import com.dedup.storage.StorageFactory.StorageType;

/**
 * @author NTF
 *
 */
public class Chunk implements Serializable{
	public int refCount;
	public StorageType type;

	public byte[] data;

	public Chunk(byte[] data) {
		this.data = data;
		this.refCount = 1;
	}

	public Chunk(StorageType type, int c) {
		this.type = type;
		this.refCount = c;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void increment() {
		this.refCount++;
	}

	public void decrement() {
		this.refCount--;
	}

	public boolean shouldDelete() {
		return this.refCount <= 0;
	}
}
