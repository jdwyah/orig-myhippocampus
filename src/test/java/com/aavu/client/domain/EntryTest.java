package com.aavu.client.domain;

import junit.framework.TestCase;

public class EntryTest extends TestCase {

	private static final String A = "<b>fsdfs dsfsdf</b>sdfsdf sdf";
	private static final String B = "<b>fsdfs dsfsdf</b>sdfsdf sdf";
	
	
	
	private static final String C = "<BODY contentEditable=true></BODY>";
	private static final String D = "<HEAD></HEAD>\n<BODY contentEditable=true>Pynchon</BODY>";
	private static final String E = "<HEAD></HEAD>\n<BODY contentEditable=true>\n<P>&nbsp;</P></BODY>";
	private static final String F = "<head></head><body contenteditable=\"true\">1</body>";
	
	public void testGetDataWithoutBodyTags() {
		Entry e = new Entry();
		e.setInnerHTML(A);		
		assertEquals(A,e.getDataWithoutBodyTags());
		
		
		e.setData(C);
		assertEquals(0,e.getDataWithoutBodyTags().length());
		
		e.setData(D);
		System.out.println("|"+e.getDataWithoutBodyTags()+"|");
		assertEquals(7,e.getDataWithoutBodyTags().length());
		
		e.setData(E);
		assertEquals(14,e.getDataWithoutBodyTags().length());
		
		e.setData(F);
		assertEquals(1,e.getDataWithoutBodyTags().length());
		
	}

}
