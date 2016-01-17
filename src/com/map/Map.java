package com.map;

import java.io.*;
import java.util.*;

import com.google.common.io.Files;

public class Map {
	public static final String LOC_PATH = "C:\\Program Files\\apache-tomcat-9.0.0.M1\\data\\pointers\\";
	
	private static final int MAX_RESULTS = 4;
	
	public static ResultHolder getResultHolderFrom(File f) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(f);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        ResultHolder r = (ResultHolder) in.readObject();
        in.close();
        fileIn.close();
        return r;
	}
	
	private static Result[] getResultsFrom(File f) {
		try {
			ResultHolder r = getResultHolderFrom(f);
	        List<Result> p = r.get(new ArrayList<Result>(), MAX_RESULTS, r);
	        return p.toArray(new Result[p.size()]);
		} catch (IOException e) {
			return new Result[0];
		} catch (ClassNotFoundException e) {
			return new Result[0];
		}
	}

	private static Result[] getResultsFromWords(String words) {
		File file = new File(LOC_PATH + words + ".ser");
		
		if (file.exists()) {
			return getResultsFrom(file);
		} else {
			return new Result[0];
		}
	}

	public static ResultBundle[] getResults(String query) {
		Result[] results = getResultsFromWords(query.toUpperCase());
		ResultBundle[] bundle = new ResultBundle[results.length];
		for (int i = 0; i < results.length; i++) {
			bundle[i] = new ResultBundle(results[i].title, results[i].description, results[i].url, results[i].thumbnail, results[i].tWidth, results[i].tHeight);
		}
		return bundle;
	}
	
	/**
	 * 
	 * @param title
	 * @param description
	 * @param thumbnail
	 * @param url
	 * @param queries The queries that the user specified the result to have.
	 * @return The id of the result
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static long addResult(String title, String description, byte[] thumbnail, String url, String[] queries, int thumbnailWidth, int thumbnailHeight, int lifetime) throws ClassNotFoundException, IOException {
		Result r = new Result(title, description, thumbnail, url, queries, thumbnailWidth, thumbnailHeight, lifetime);
		for (String s : queries) {
			File f = new File(LOC_PATH + s.toUpperCase() + ".ser");
			if (f.exists()) {
				ResultHolder rh = getResultHolderFrom(f);
				rh.add(r);
				FileOutputStream fo = new FileOutputStream(f);
				ObjectOutputStream o = new ObjectOutputStream(fo);
				o.writeObject(rh);
				o.close();
				fo.close();
			} else {
				Files.createParentDirs(f);
				Files.touch(f);
				ResultHolder rh = new ResultHolder(r, null);
				FileOutputStream fo = new FileOutputStream(f);
				ObjectOutputStream o = new ObjectOutputStream(fo);
				o.writeObject(rh);
				o.close();
				fo.close();
			}
		}
		return r.id;
	}
}
