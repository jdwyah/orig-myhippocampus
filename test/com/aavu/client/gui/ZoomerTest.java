package com.aavu.client.gui;

import com.google.gwt.junit.client.GWTTestCase;

import junit.framework.TestCase;

public class ZoomerTest extends GWTTestCase {

	public void testScaling(){
		
		Zoomer z = new Zoomer(null);
		
		
		for (int i = 0; i < 11; i++) {
//			System.out.println("i "+i);
//			double d = Zoomer.convertToScale(i);
//			System.out.println("scale: "+d);
//			System.out.println("backToI: "+Zoomer.convertFromScale(d));
//			System.out.println("--------");
						
			assertEquals(i,z.convertFromScale(Zoomer.convertToScale(i)));	
		}		
		
	}
//	
	public String getModuleName() {		
		return "com.aavu.HippoTest";
	}
}
