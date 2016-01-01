package com.map;

import java.io.Serializable;

public class InputBundle implements Serializable {
	private static final long serialVersionUID = 1202050208549670217L;
	public InputBundle(boolean writing, String contents) {
		this.writing = writing;
		this.contents = contents;
	}
	private boolean writing;
	private String contents;
	public boolean isWriting() {
		return writing;
	}
	public String getContents() {
		return contents;
	}
}
