package com.aavu.client.gui.dhtmlIslands;

public class Type {
	
	public static final int MAX_SIZE = 60;
	
	public static int SPACING_30 = 10;
	public static int SPACING_60 = 18;
	public static int SPACING_100 = 33;
	
	public int img_width;
	public int img_height;
	public int img_spacing_w;
	public int img_spacing_h;
	public String prefix;
	public int numImages;
	
	public Type(int img_width, int img_height, int img_spacing_w, int img_spacing_h, String prefix, int numImages) {
		super();
		this.img_width = img_width;
		this.img_height = img_height;
		this.img_spacing_w = img_spacing_w;
		this.img_spacing_h = img_spacing_h;
		this.prefix = prefix;
		this.numImages = numImages;
	}
	
}
