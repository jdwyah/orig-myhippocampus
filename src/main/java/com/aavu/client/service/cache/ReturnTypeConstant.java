package com.aavu.client.service.cache;

public class ReturnTypeConstant {

	private int i;

	/**
	 * Typesafe constant pattern
	 * 
	 * @param i
	 */
	public ReturnTypeConstant(int i) {
		this.i = i;
	}

	public int getValue(){
		return i;
	}
}
