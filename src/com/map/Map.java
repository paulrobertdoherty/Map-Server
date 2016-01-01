package com.map;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServlet;

import com.map.ResultHolder.Pointer;

public class Map extends HttpServlet {
	private static final long serialVersionUID = -1750103788596339179L;

	private static final String LOC_PATH = "/";

	private static String getPath(String[] words, int i) {
		if (i > 0) {
			return getPath(words, i - 1) + "/" + words;
		}
		return words[i];
	}
	
	private static Result[] getResultsFrom(File f) {
		try {
			FileInputStream fileIn = new FileInputStream(f);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        ResultHolder r = (ResultHolder) in.readObject();
	        in.close();
	        fileIn.close();
	        
	        List<Result> p = get(new ArrayList<Result>(), 4, r);
	        return (Result[])p.toArray();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
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
			return null;
		}
	}

	public static ResultBundle[] getResults(String query) {
		Result[] results = getResultsFromWords(query.split(" "));
		
		if (results != null) {
			List<ResultBundle> s = new ArrayList<ResultBundle>();
			for (Result r : results) {
				s.add(new ResultBundle(r.title, r.description, r.url, r.thumbnail));
			}
			return (ResultBundle[])s.toArray();
		} else {
			return new ResultBundle[]{};
		}
	}
}
