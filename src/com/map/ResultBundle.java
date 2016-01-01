package com.map;

public class ResultBundle {
	private String title, description, thumbnail;
	private long id;
	
	public ResultBundle(String title, String description, String thumbnail, long id) {
		this.title = title;
		this.description = description;
		this.thumbnail = thumbnail;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public long getId() {
		return id;
	}
}