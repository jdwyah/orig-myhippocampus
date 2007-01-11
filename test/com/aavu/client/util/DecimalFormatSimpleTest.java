package com.aavu.client.util;

import junit.framework.TestCase;

public class DecimalFormatSimpleTest extends TestCase {

	public void testSimple(){
		DecimalFormatSimple dfc = new DecimalFormatSimple(2);
		
		assertEquals("2.34", dfc.format(2.34545));
		assertEquals("2234.13", dfc.format(2234.134324));
		
		
		DecimalFormatSimple dfc3 = new DecimalFormatSimple(3);
		
		assertEquals("-2.345", dfc3.format(-2.34545));
		assertEquals("-2234.134", dfc3.format(-2234.134324));
		
		
		assertEquals("-.345", dfc3.format(-0.34545));
		assertEquals(".345", dfc3.format(0.34545));
		
		
		assertEquals("1", dfc3.format(1));
		assertEquals("214", dfc3.format(214));
		
		assertEquals("3.2423412342314233E21", dfc3.format(3242341234231423423431.0));
		assertEquals("-1.23123123123123123E18", dfc3.format(-1231231231231231231.213123123123121));
		
		assertEquals(".123", dfc3.format(.1231231231231231231213123123123121));
			
		assertEquals("-.123", dfc3.format(-.12343242332423412342314234234310));
		
		
		//Weird  .1%1 -> .10000
		//but    -.1%1 -> -.1   (no 0's)
		assertEquals("1.120", dfc3.format(1.12));
		assertEquals("1.100", dfc3.format(1.1));
		assertEquals("-.1", dfc3.format(-.1));
	}
}
