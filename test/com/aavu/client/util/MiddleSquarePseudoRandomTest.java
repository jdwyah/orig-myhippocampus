package com.aavu.client.util;

import junit.framework.TestCase;

public class MiddleSquarePseudoRandomTest extends TestCase {

	public void testNextDouble() {
		PsuedoRandom pr = new MiddleSquarePseudoRandom(123,4);
		System.out.println("n: "+pr.nextDouble());
		System.out.println("n: "+pr.nextDouble());
		System.out.println("n: "+pr.nextDouble());
		System.out.println("n: "+pr.nextDouble());
		System.out.println("n: "+pr.nextDouble());
		System.out.println("n: "+pr.nextDouble());
		System.out.println("n: "+pr.nextDouble());
		System.out.println("n: "+pr.nextDouble());
	}

	public void testNextInt() {
		PsuedoRandom pr = new MiddleSquarePseudoRandom(123,4);
		System.out.println("n: "+pr.nextInt(10));
		System.out.println("n: "+pr.nextInt(10));
		System.out.println("n: "+pr.nextInt(10));
		System.out.println("n: "+pr.nextInt(10));
		System.out.println("n: "+pr.nextInt(10));
	}

}
