package com.aavu.server.lucene.util;

import junit.framework.TestCase;

public class HTMLConverterTest extends TestCase {

	public void testFromString() {
		HTMLConverter convert = new HTMLConverter();
		
		String res = (String) convert.fromString("<HTML><BODY>foo</BODY></HTML>", null);
		assertEquals("foo", res);
		
		res = (String) convert.fromString("<HTML><BODY>A B C</BODY></HTML>", null);
		assertEquals("A B C", res);
		
		//TODO notice no space betwen AA&B
		res = (String) convert.fromString("<HTML><BODY>A <a href=\"\">AA</A>B C</BODY></HTML>", null);
		assertEquals("A AAB C", res);
		
		res = (String) convert.fromString("<HTML><BODY><A href=\"http://www.dsfs.so\">Link</A> <font type=\"arial\"><B>Foo</B></font>45435</BODY></HTML>", null);
		assertEquals("LinkFoo45435", res);
		
		res = (String) convert.fromString("<HEAD></HEAD>\n"+
		"<BODY contentEditable=true><FONT size=2>\n"+
		"<P>At Full Cuty, and extemely balanced coffee, herby, caramelly, with rustic hints</P></FONT></BODY>\n",null);		
		assertEquals("At Full Cuty, and extemely balanced coffee, herby, caramelly, with rustic hints ", res);
		
	}

}
