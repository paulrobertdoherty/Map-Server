package com.map;

public class ResultBundle {
	private String title, description, url;
	private byte[] thumbnail;
	private int width, height;
	
	public ResultBundle(String title, String description, String url, byte[] thumbnail, int width, int height) {
		this.title = title;
		this.description = description;
		this.thumbnail = thumbnail;
		this.url = url;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public String getUrl() {
		return url;
	}
}