package com.aavu.server.util;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * PEND low Pretty weak test. Pretty weak class.
 * 
 * @author Jeff Dwyer
 * 
 */
public class RandomUtilsTest extends TestCase {

	private static final Logger log = Logger.getLogger(RandomUtilsTest.class);
	private static final int L = 20;


	public void testRandomstring() {
		for (int i = 0; i < 5; i++) {
			String s = RandomUtils.randomstring(L);
			assertEquals(L, s.length());
			log.debug(i + " " + s);
		}

	}

}
