package com.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Thumbnail implements Serializable {
	private static final long serialVersionUID = -2220481333566647794L;
	private static transient final int WIDTH = 256, HEIGHT = 256;
	private int[][] colors = new int[WIDTH][HEIGHT];
	
	public Thumbnail(String url) {
		
	}
	
	public Thumbnail(Image image) {
		BufferedImage buffer = null;
		
		if (image instanceof BufferedImage) {
			buffer = (BufferedImage)image;
		} else {
			buffer = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		}
		
		for (int x = 0; x < buffer.getWidth(); x++) {
			for (int y = 0; y < buffer.getHeight(); y++) {
				if (buffer.getWidth() == WIDTH && buffer.getHeight() == HEIGHT) {
					colors[x][y] = buffer.getRGB(x, y);
				} else {
					//Scales the image. Really hope this is never used.  Still works, but just looks ugly
					int nx = (int)(((float)(x) / (float)buffer.getWidth()) * WIDTH);
					int ny = (int)(((float)(y) / (float)buffer.getHeight()) * HEIGHT);
					colors[nx][ny] = buffer.getRGB(x, y);
				}
			}
		}
	}
}