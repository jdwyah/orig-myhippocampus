package com.aavu.client.gui.ocean.dhtmlIslands;

public class Type {
	
	
	public static int SIZE_30 = 30;
	public static int SPACING_30 = 10;
	public static int SPACING_60 = 18;
	public static int SPACING_100 = 33;
	
	
	public static int SIZE_120 = 30;
	public static int SPACING_120 = 10;
	
	
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
