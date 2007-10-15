package com.aavu.server.domain;

import java.util.Date;

import junit.framework.TestCase;

public class DeliciousPostTest extends TestCase {

	public void testGetTags() {
		String tags = "foo foo2 foo3";
		DeliciousPost dp = new DeliciousPost("title", "http:", tags, "data", null, new Date());
		assertEquals(3, dp.getTags().length);

		System.out.println("\n\n\n\n");

		tags = "foo \"foo2 with space\" foo3";
		dp = new DeliciousPost("title", "http:", tags, "data", null, new Date());
		assertEquals(3, dp.getTags().length);


		tags = "foo \"foo2, with space\" foo3 sdf 2@#$@#*( \"sadfs sadf sdj923 234`2\"";
		dp = new DeliciousPost("title", "http:", tags, "data", null, new Date());
		assertEquals(6, dp.getTags().length);

	}

}
