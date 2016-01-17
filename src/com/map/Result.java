package com.map;

import java.io.*;
import java.nio.charset.Charset;

import com.google.common.io.Files;

public class Result implements Serializable, Comparable<Result> {
	private static final long serialVersionUID = -5750724440003169986L;
	
	public static transient final String RESULT_PATH = "C:\\Program Files\\apache-tomcat-9.0.0.M1\\data\\results\\", LARGEST_FILE_LOC = RESULT_PATH + "largest_file_location.txt";
	
	public String title, description, url;
	public byte[] thumbnail;
	public String[] solved;
	public int quality = 0, lifetime, tWidth, tHeight;
	public long id;

	public Result(String title, String description, byte[] thumbnail, String url, String[] solved, int thumbnailWidth, int thumbnailHeight, int lifetime) throws IOException {
		this.title = title;
		this.description = description;
		this.thumbnail = thumbnail;
		this.tWidth = thumbnailWidth;
		this.tHeight = thumbnailHeight;
		this.url = url;
		this.solved = solved;
		this.lifetime = lifetime;
		this.id = getNextId();
	}
	
	public static long getNextId() throws IOException {
		File loc = new File(LARGEST_FILE_LOC);
		try {
			long r = Long.parseLong(Files.readFirstLine(loc, Charset.defaultCharset()));
			r++;
			Files.write(new String(r + "").getBytes(), loc);
			return r;
		} catch (FileNotFoundException e) {
			Files.write(new String(0 + "").getBytes(Charset.defaultCharset()), loc);
			return 0;
		}
		
	}

	@Override
	public int compareTo(Result r) {
		return quality - r.quality;
	}
}