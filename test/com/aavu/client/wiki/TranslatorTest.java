package com.aavu.client.wiki;

import junit.framework.TestCase;

public class TranslatorTest extends TestCase {

	public void testToHTML() {
		
		
		
		String in = "###His Life###\n"+
		"##Birth##\n"+
		"All about his birth\n"+

		"##Career##\n"+
		"#Thing 1#\n"+
		"Thing 1 details\n"+

		"#Thing 2#\n"+
		"Thing 2 details";
		
		String rtn = Translator.toHTML(in);
		
		System.out.println("in "+in);
		System.out.println("rtn"+rtn);
		
		System.out.println("\n\n");
		
		in = "dfofood ##dogs##   dsfsdfs";
		rtn = Translator.toHTML(in);		
		System.out.println("in "+in);
		System.out.println("rtn"+rtn);
		
		in = "my #string# ";
		
		rtn = Translator.toHTML(in);
		System.out.println("\n\n");
		System.out.println("in "+in);
		System.out.println("rtn"+rtn);
	}

}
