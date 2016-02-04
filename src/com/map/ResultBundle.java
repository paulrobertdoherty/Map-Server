package com.map;

public class ResultBundle {
	private String title, description, url, thumbnail;
	
	public ResultBundle(String title, String description, String url, String thumbnail) {
		this.title = title;
		this.description = description;
		this.thumbnail = thumbnail;
		this.url = url;
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

	public String getUrl() {
		return url;
	}
}