package com.map;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServlet;

import com.map.ResultHolder.Pointer;

public class Map extends HttpServlet {
	private static final long serialVersionUID = -1750103788596339179L;

	private static final String LOC_PATH = "/";
	
	private static final int MAX_RESULTS = 4;
	
	

	private static String getPath(String[] words, int i) {
		if (i > 0) {
			return getPath(words, i - 1) + "/" + words;
		}
		return words[i];
	}
	
	private static ResultHolder getResultHolderFrom(File f) throws IOException, ClassNotFoundException {
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
	        List<Result> p = get(new ArrayList<Result>(), MAX_RESULTS, r);
	        return (Result[])p.toArray();
		} catch (IOException e) {
			return new Result[0];
		} catch (ClassNotFoundException e) {
			return new Result[0];
		}
	}
	
	private static List<Result> get(List<Result> l, int size, Object result) throws IOException, ClassNotFoundException {
		if (result instanceof ResultHolder) {
			ResultHolder r = (ResultHolder)result;
			if (r.greatest() == null) {
				if (r.least() == null) {
					return l;
				}
				l.add(convert((Pointer)r.least()));
				return l;
			} else if (r.greatest() instanceof Pointer) {
				l.add(convert((Pointer)r.greatest()));
				if (size > 1) {
					Object p = r.least();
					if (p instanceof Pointer) {
						l.add(convert((Pointer)p));
						return l;
					}
					return get(l, size - 1, r.least());
				}
				return l;
			} else {
				if (size > 1) {
					return get(l, size - 1, r.greatest());
				}
				return l;
			}
		} else {
			Pointer p = (Pointer)result;
			l.add(convert(p));
			return l;
		}
	}

	private static Result convert(Pointer p) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(Result.RESULT_PATH + p.pointer + ".ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Result r = (Result) in.readObject();
        in.close();
        fileIn.close();
		return r;
	}

	private static Result[] getResultsFromWords(String[] words) {
		File file = new File(LOC_PATH + getPath(words, words.length - 1) + ".ser");
		
		if (file.exists()) {
			return getResultsFrom(file);
		} else {
			return new Result[0];
		}
	}

	public static ResultBundle[] getResults(String query) {
		Result[] results = getResultsFromWords(query.split(" "));
		ResultBundle[] bundle = new ResultBundle[results.length];
		for (int i = 0; i < results.length; i++) {
			bundle[i] = new ResultBundle(results[i].title, results[i].description, results[i].thumbnail, results[i].id);
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
	public static long addResult(String title, String description, String thumbnail, String url, String[] queries) throws ClassNotFoundException, IOException {
		Result r = new Result(title, description, thumbnail, url, queries);
		for (String s : queries) {
			String[] words = s.split(" ");
			File f = new File(LOC_PATH + getPath(words, words.length - 1) + ".ser");
			if (f.exists()) {
				ResultHolder rh = getResultHolderFrom(f);
				rh.add(r);
				FileOutputStream fo = new FileOutputStream(f);
				ObjectOutputStream o = new ObjectOutputStream(fo);
				o.writeObject(rh);
				o.close();
				fo.close();
			} else {
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
