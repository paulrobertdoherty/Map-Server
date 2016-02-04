package com.map;

import java.io.*;

public class Result implements Serializable, Comparable<Result> {
	private static final long serialVersionUID = -5750724440003169986L;
	
	public static transient final String RESULT_PATH = "C:\\Program Files\\apache-tomcat-9.0.0.M1\\data\\results\\", LARGEST_FILE_LOC = RESULT_PATH + "largest_file_location.txt";
	private static transient final File loc = new File(LARGEST_FILE_LOC);
	
	public String title, description, url, thumbnail;
	public String[] solved;
	public int quality = 0, lifetime, tWidth, tHeight;
	public long id;

	public Result(String title, String description, String thumbnail, String url, String[] solved, int lifetime) throws IOException {
		this.title = title;
		this.description = description;
		this.thumbnail = thumbnail;
		this.url = url;
		this.solved = solved;
		this.lifetime = lifetime;
		this.id = getNextId();
	}
	
	private long getNextId() throws IOException {
		try {
			long r = getId();
			r++;
			setId(r);
			return r;
		} catch (IOException e) {
			setId(0);
			return 0;
		}
	}
	
	public static long getId() throws IOException {
		BufferedReader b = new BufferedReader(new FileReader(loc));
		String id = b.readLine();
		b.close();
		return Long.parseLong(id);
	}
	
	private void setId(long id) throws IOException {
		BufferedWriter w = new BufferedWriter(new FileWriter(loc));
		w.flush();
		w.write(id + "");
		w.flush();
		w.close();
	}

	@Override
	public int compareTo(Result r) {
		return quality - r.quality;
	}
}