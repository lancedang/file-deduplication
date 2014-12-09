package com.dedup;

import java.io.Serializable;
import java.util.*;

public class Index implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Key = file pathname Value = List of fingerprint of chunks , in-order
	 */
	public TreeMap<String, ArrayList<String>> files;

	/**
	 * Key = Chunk fingerprint Value = Reference Count
	 */
	public TreeMap<String, Integer> chunks;

	public Index() {
		this.files = new TreeMap<String, ArrayList<String>>();
		this.chunks = new TreeMap<String, Integer>();
	}
}
