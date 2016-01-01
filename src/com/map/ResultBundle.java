package com.map;

import java.io.Serializable;

public class ResultBundle implements Serializable {
	private static final long serialVersionUID = 4084669087177217547L;
	public String title, description, url;
	public Thumbnail thumbnail;
	
	public ResultBundle(String title, String description, String url, Thumbnail thumbnail) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.thumbnail = thumbnail;
	}
}