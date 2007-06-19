package com.aavu.client.gui.ocean.dhtmlIslands;

public class AcreSize {
	
	public static final AcreSize SIZE_120 = new AcreSize(120,4);
	public static final AcreSize SIZE_100 = new AcreSize(100,1);	
	public static final AcreSize SIZE_60 = new AcreSize(60,1);
	public static final AcreSize SIZE_30 = new AcreSize(30,1);
	
	private int size;
	private int scalar;

	public AcreSize(int i,int scalar) {
		this.size = i;
		this.scalar = scalar;
	}

	public int getSize() {
		return size;
	}

	public int getScalar() {		
		return scalar;
	}

	
	
}
