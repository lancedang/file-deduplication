/**
 * 
 */
package com.dedup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.dedup.storage.IStorage;
import com.dedup.storage.StorageFactory;
import com.dedup.storage.StorageFactory.StorageType;
import com.microsoft.windowsazure.services.core.storage.StorageException;

/**
 * @author NTF
 *
 */
public class MyDedup {

	/**
	 * Parameter Bags for Cli request
	 * 
	 * @author NTF
	 */
	public static class RequestParameters {
		public static enum RequestAction {
			UPLOAD, DOWNLOAD, DELETE;
		};

		public RequestAction action;

		/**
		 * the window size (in bytes)
		 */
		public int m;
		/**
		 * the modulo parameter
		 */
		public int q;
		/**
		 * the maximum chunk size (in bytes)
		 */
		public int x;
		/**
		 * the base parameter
		 */
		public int d;
		/**
		 * the matching value that determines if a rabin fingerprint is an
		 * anchor point
		 */
		public int v;

		public StorageType storage;

		public String pathName;

		public RequestParameters(RequestAction action, int m, int q, int x,
				int d, int v, String pathName, StorageType type) {
			this.action = action;
			this.m = m;
			this.q = q;
			this.x = x;
			this.d = d;
			this.v = v;
			this.pathName = pathName;
			this.storage = type;
		}

		public RequestParameters(RequestAction action, String pathName) {
			this.action = action;
			this.pathName = pathName;
		}
	}

	public static final String storageConnectionString = "DefaultEndpointsProtocol=https;"
			+ "AccountName=portalvhdsnjtkkbt6f62w1;"
			+ "AccountKey=i7/FNq8qQU/yyRxfqWlAGg73AWKtBUiWDkMfBA8uIXvU5RVVQzr487ZSEpBUAMj/2JRWNatQEM6DDPTv3c5Rcw==";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		RequestParameters request = MyDedup.handleArgs(args);

		if (request == null) {
			/*
			 * testcase1 upload 1024 4096 2048 64 1 testcases/rfp_anchor_mac
			 * local
			 */
			System.out
					.println("MyDedup - by @ntf, @longyakmok , @kousin197804");
			System.out.println();
			System.out
					.println("upload <m> <q> <x> <d> <v> <file_to_upload> <local|remote>");
			System.out.println("    m: the window size (in bytes)");
			System.out.println("    q: the modulo parameter");
			System.out.println("    x: the maximum chunk size (in bytes)");
			System.out.println("    d: the base parameter");
			System.out
					.println("    v: the matching value that determines if a rabin fingerprint is an anchor point");
			System.out.println("download <file_to_download>");
			System.out.println("delete <file_to_delete>");
			return;
		}

		try {

			StorageFactory.createStorage(StorageFactory.StorageType.LOCAL,
					"data/");
			StorageFactory.createStorage(StorageFactory.StorageType.AZURE,
					storageConnectionString);
			IStorage storage = StorageFactory.getStorage(request.storage);

			// open or create index
			Index index = MyDedup.indexExists() ? MyDedup.open() : new Index();

			switch (request.action) {
			case DELETE:
				deleteAction(request, System.out, index);
				break;
			case DOWNLOAD:
				downloadAction(request, System.out, index);
				break;
			case UPLOAD:
				uploadAction(request, System.out, index, storage);
				break;
			default:
				break;
			}

			// save index
			MyDedup.save(index);

			System.out.println("[DEBUG] =========end=========");

		} catch (InvalidKeyException | URISyntaxException | StorageException
				| IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void uploadAction(RequestParameters request, PrintStream out,
			Index index, IStorage storage) throws IOException {
		File file = new File(request.pathName);
		if (!file.exists()) {
			throw new FileNotFoundException("input file not found.");
		}
		FileReader in = new FileReader(request.pathName);

		int b;
		ArrayList<Integer> offsets = new ArrayList<Integer>();

		offsets.add(0);

		int size = 0;
		int rfp = 0;
		// @see tut9_assg3-rfp.pdf p.31

		// Rabin Fingerprint here
		while ((b = in.read()) != -1) {

			// we hit the maximum chunk size , we must put anchor point here
			if (size == request.x) {
				// TODO
			}

			// this is our interested value, put anchor point here
			if (rfp == request.v) {
				// TODO
			}
			size++;
			// we read one byte b
			// calculate the RFP

		}

		// size = file size here
		in.close();
	}

	public static void downloadAction(RequestParameters request,
			PrintStream out, Index index) throws InvalidKeyException,
			URISyntaxException, StorageException, IOException {

		if (!index.hasFile(request.pathName)) {
			throw new FileNotFoundException("File not found");
		}
		FileOutputStream output = new FileOutputStream(new File(
				request.pathName));

		// SHA-1 String , Chunk pair
		for (Entry<String, Chunk> pair : index.getChunks(request.pathName)) {
			IStorage storage = StorageFactory.getStorage(pair.getValue().type);
			Chunk chunkData = storage.get(pair.getKey());
			output.write(chunkData.data);
		}
		output.close();
	}

	public static void deleteAction(RequestParameters request, PrintStream out,
			Index index) throws FileNotFoundException, InvalidKeyException,
			URISyntaxException, StorageException {
		ArrayList<Map.Entry<String, Chunk>> deleteList = new ArrayList<Map.Entry<String, Chunk>>();
		if (!index.hasFile(request.pathName)) {
			throw new FileNotFoundException("File not found");
		}

		for (Entry<String, Chunk> pair : index.getChunks(request.pathName)) {
			pair.getValue().decrement();
			if (pair.getValue().shouldDelete()) {
				deleteList.add(pair);
			} else {
				index.chunks.put(pair.getKey(), pair.getValue());
			}
		}

		for (Entry<String, Chunk> pair : deleteList) {
			out.println("[DEBUG] " + pair.getKey() + " removed from "
					+ pair.getValue().type.toString());
			IStorage storage = StorageFactory.getStorage(pair.getValue().type);
			storage.remove(pair.getKey());
			index.chunks.remove(pair.getKey());
		}
		index.files.remove(request.pathName);
		out.println("Delete done and " + deleteList.size()
				+ " chucks are removed");
	}

	public static String calculateFingerprint(byte[] data) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(data);
			byte[] checksum = md.digest();
			return new BigInteger(1, checksum).toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static RequestParameters handleArgs(String[] args) {
		if (args.length < 1) {
			return null;
		}

		switch (args[0]) {
		case "upload":
			if (args.length != 8) {
				return null;
			}
			StorageFactory.StorageType type = args[7].equals("local") ? StorageFactory.StorageType.LOCAL
					: StorageFactory.StorageType.AZURE;
			return new MyDedup.RequestParameters(
					MyDedup.RequestParameters.RequestAction.UPLOAD,
					Integer.parseInt(args[1]), Integer.parseInt(args[2]),
					Integer.parseInt(args[3]), Integer.parseInt(args[4]),
					Integer.parseInt(args[5]), args[6], type);
		case "download":
			if (args.length != 2) {
				return null;
			}
			return new MyDedup.RequestParameters(
					MyDedup.RequestParameters.RequestAction.DOWNLOAD, args[1]);
		case "delete":
			if (args.length != 2) {
				return null;
			}
			return new MyDedup.RequestParameters(
					MyDedup.RequestParameters.RequestAction.DELETE, args[1]);
		}
		return null;
	}

	public static void save(Index index) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				"mydedup.meta"));
		oos.writeObject(index);
		oos.close();
	}

	public static boolean indexExists() {
		File file = new File("mydedup.meta");
		return file.exists();
	}

	static public Index open() throws IOException, ClassNotFoundException {

		ObjectInputStream objectinputstream = new ObjectInputStream(
				new FileInputStream("mydedup.meta"));
		Index index = (Index) objectinputstream.readObject();
		objectinputstream.close();

		return index;

	}

}
