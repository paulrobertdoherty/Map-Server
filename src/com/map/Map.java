package com.map;

import java.io.*;
import java.util.*;

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
	
	/**
	 * Interweaves two EQUALLY SIZED arrays of results.
	 * @param a
	 * @param b
	 * @return
	 */
	private static Result[] interweave(Result[] a, Result[] b) {
		Result[] toReturn = new Result[a.length + b.length];
		for (int i = 0; i < toReturn.length; i++) {
			if (i % 2 == 0) {
				toReturn[i] = a[i / 2];
			} else {
				toReturn[i] = b[i / 2];
			}
		}
		return toReturn;
	}

	private static Result[] getResultsFromWords(String words) {
		File file = new File(LOC_PATH + words + ".ser");
		
		try {
			if (file.exists()) {
				ResultHolder r = getResultHolderFrom(file);
		        List<Result> p = r.get(new ArrayList<Result>(), MAX_RESULTS / 2, r);
		        Result[] results = p.toArray(new Result[p.size()]);
		        return interweave(results, ResultHolder.getRandom(MAX_RESULTS / 2, results, words, r));
			} else {
				return ResultHolder.getRandom(MAX_RESULTS, new Result[0], words, null);
			}
		} catch (IOException e) {
			return new Result[0];
		} catch (ClassNotFoundException e) {
			return new Result[0];
		}
	}

	public static ResultBundle[] getResults(String query) {
		Result[] results = getResultsFromWords(query.toUpperCase());
		ResultBundle[] bundle = new ResultBundle[results.length];
		for (int i = 0; i < results.length; i++) {
			bundle[i] = new ResultBundle(results[i].title, results[i].description, results[i].url, results[i].thumbnail);
		}
		return bundle;
	}
	
	public static void addResult(Result r, String query) throws IOException, ClassNotFoundException {
		File f = new File(LOC_PATH + query.toUpperCase() + ".ser");
		if (f.exists()) {
			ResultHolder rh = getResultHolderFrom(f);
			rh.add(r);
			FileOutputStream fo = new FileOutputStream(f);
			ObjectOutputStream o = new ObjectOutputStream(fo);
			o.writeObject(rh);
			o.close();
			fo.close();
		} else if (f.createNewFile()){
			ResultHolder rh = new ResultHolder(r, null);
			FileOutputStream fo = new FileOutputStream(f);
			ObjectOutputStream o = new ObjectOutputStream(fo);
			o.writeObject(rh);
			o.close();
			fo.close();
		}
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
	public static long addResult(String title, String description, String thumbnail, String url, String[] queries, int lifetime) throws ClassNotFoundException, IOException {
		Result r = new Result(title, description, thumbnail, url, queries, lifetime);
		for (String s : queries) {
			addResult(r, s);
		}
		return r.id;
	}
}
