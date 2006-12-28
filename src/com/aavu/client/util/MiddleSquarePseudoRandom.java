package com.aavu.client.util;

public class MiddleSquarePseudoRandom implements PsuedoRandom {
	
	private long curVal;
	private int size;
	private long sizeMax;

	/**
	 * See
	 * http://en.wikipedia.org/wiki/Middle-square_method
	 * Not a problem free implementation
	 * 
	 * @param seed
	 * @param size
	 */
	public MiddleSquarePseudoRandom(long seed,int size){
		this.curVal = seed;
		this.size = size;
		this.sizeMax = (long) Math.pow(10, size);		
		System.out.println("size m "+sizeMax);
	}

	public double nextDouble() {

		//System.out.println("cur v"+curVal);
		curVal = (long) Math.pow(curVal, 2);
		//System.out.println("sq "+curVal);
		
		Long l = new Long(curVal);
		String s = l.toString();
		while(s.length() < size){			
			s += "7";
		}
		int zeros = s.indexOf("00");		
		if(-1 != zeros){
			s = s.replace('0', '3');		
		}
		//System.out.println("padded "+s);
		
		int start = (s.length() - size) / 2;		
		//System.out.println("start "+start+" start+s "+(start+size));
		curVal = Long.parseLong(s.substring(start, start+size));
		//System.out.println("next "+curVal);
		
		return (double)curVal/sizeMax;
	}

	public int nextInt(int max) {
		return (int) (nextDouble() * max);
	}

}
