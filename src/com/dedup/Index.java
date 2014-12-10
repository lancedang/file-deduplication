package com.dedup;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.MutablePair;

public class Index implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Key = file pathname Value = List of fingerprint of chunks , in-order
	 */
	public TreeMap<String, ArrayList<String>> files;

	/**
	 * Key = Chunk fingerprint Value = instance of Chunk
	 */
	public TreeMap<String, Chunk> chunks;

	public Index() {
		this.files = new TreeMap<String, ArrayList<String>>();
		this.chunks = new TreeMap<String, Chunk>();
	}

	public boolean hasFile(String pathName) {
		return files.containsKey(pathName);
	}

	public ArrayList<Entry<String, Chunk>> getChunks(String pathName) {
		ArrayList<Map.Entry<String, Chunk>> chunks = new ArrayList<Map.Entry<String, Chunk>>();
		for (String hash : files.get(pathName)) {
			chunks.add(new MutablePair<String, Chunk>(hash, this.getChunk(hash)));
		}
		return chunks;
	}

	public HashMap<String, Chunk> getUniqueChunks(String pathName) {
		HashMap<String, Chunk> chunks = new HashMap<String, Chunk>();
		for (String hash : files.get(pathName)) {
			chunks.put(hash, this.getChunk(hash));
		}
		return chunks;
	}

	public ArrayList<String> getChunkHashes(String pathName) {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		for (String fingerprint : files.get(pathName)) {
			chunks.add(this.getChunk(fingerprint));
		}
		return files.get(pathName);
	}

	public boolean hasChunk(String hash) {
		return chunks.containsKey(hash);
	}

	public Chunk getChunk(String hash) {
		return chunks.get(hash);
	}
}
