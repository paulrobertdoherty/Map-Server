package com.map;

import java.io.*;

public class Result implements Serializable, Comparable<Result> {
	private static final long serialVersionUID = -5750724440003169986L;
	
	public static transient final String RESULT_PATH = "/", LARGEST_FILE_LOC = "largest_file_location.txt";
	
	public String title, description, url;
	public Thumbnail thumbnail;
	public String[] solved;
	public int quality = 0;
	public long id;

	public Result(String title, String description, String thumbnailLoc, String url, String[] solved) {
		this.title = title;
		this.description = description;
		this.thumbnail = new Thumbnail(thumbnailLoc);
		this.url = url;
		this.solved = solved;
		this.id = getNextId();
	}
	
	public Result(String title, String description, Thumbnail thumbnail, String url, String[] solved) {
		this.title = title;
		this.description = description;
		this.thumbnail = thumbnail;
		this.url = url;
		this.solved = solved;
		this.id = getNextId();
	}
	
	public static long getNextId() {
		try {
			FileReader f = new FileReader(RESULT_PATH + LARGEST_FILE_LOC);
			long r = 0;
			int d = f.read();
			while (d != -1) {
				r = (r * 10) + d;
			}
			f.close();
			r++;
			return r;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int compareTo(Result r) {
		return quality - r.quality;
	}
}