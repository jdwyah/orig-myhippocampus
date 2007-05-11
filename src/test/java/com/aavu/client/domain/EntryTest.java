package com.aavu.client.domain;

import junit.framework.TestCase;

public class EntryTest extends TestCase {

	private static final String A = "<b>fsdfs dsfsdf</b>sdfsdf sdf";
	private static final String B = "<b>fsdfs dsfsdf</b>sdfsdf sdf";
	
	
	
	private static final String C = "<BODY contentEditable=true></BODY>";
	private static final String D = "<HEAD></HEAD>\n<BODY contentEditable=true>Pynchon</BODY>";
	private static final String E = "<HEAD></HEAD>\n<BODY contentEditable=true>\n<P>&nbsp;</P></BODY>";
	private static final String F = "<head></head><body contenteditable=\"true\">1</body>";
	
	private static final String G = "A comparison of the two individuals that solved two of the most complex problems. Andrew Wyles &amp; Fermat's last equation and <a href=\"http://en.wikipedia.org/wiki/Michael_Ventris\" title=\"Michael Ventris\">Michael Ventris </a>with Linear B.<br><br>Best graffiti found in nyc subway? \"I solved Fermat's last theorem, but my train is coming\"<br><br><br>Mastery of any subject takes 10,000 hours. Piano, programming, etc. 3hr/day -&gt; 10 years.<br><br>Most important trait for Wyles was \"Stubbornness\"&nbsp; \"Synthesization\" &amp; effort. <br><br>We spend too much time obsessing about the top of the curve, when 13 v. smart people are more suited to solve today's problems than 1 genius. <br><br><br>";	
	private static final String H = "Cardiologist @ Ottawa Heart<br><br>Just built his wine cellar. Very nice guy. Discussed some interesting software that might exist for medical imaging.<br><br>";
	
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
		
		e.setData(G);
		assertEquals(G.length(),e.getDataWithoutBodyTags().length());
		e.setData(H);
		assertEquals(H.length(),e.getDataWithoutBodyTags().length());
	}

}
