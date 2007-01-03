package com.aavu.client.gui.dhtmlIslands;

public class AcreSize {
	
	public static final AcreSize SIZE_100 = new AcreSize(100);
	public static final AcreSize SIZE_60 = new AcreSize(60);
	public static final AcreSize SIZE_30 = new AcreSize(30);
	
	private int size;

	public AcreSize(int i) {
		this.size = i;
	}

	public int getSize() {
		return size;
	}

	
	
}
