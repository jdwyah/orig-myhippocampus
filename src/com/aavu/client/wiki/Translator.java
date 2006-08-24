package com.aavu.client.wiki;

public class Translator {

	public String translate(String wikiText){
		
		String rtn = "";
		
		return rtn;
		
	}

	public static String toHTML(String text) {

		if(text == null){
			text = "";
		}
		
		String rtn = text.replaceAll("###(.*)###", "<h1>$1</h1>");
		String rtn1 = rtn.replaceAll("##(.*)##", "<h2>$1</h2>");
		String rtn2 = rtn1.replaceAll("#(.*)#", "<h3>$1</h3>");
		
		//String rtn1 = rtn.replaceAll("[^#]##([^#]*)##[^#]", "<h2>$1</h2>");
		//
		
		String rtn3 = rtn2.replaceAll("\\n", "<BR>");
		
		//String rtn4 = rtn3.replaceAll("^", "<BR2>");
		//String rtn4 = rtn3.replaceAll("\t", "<LI>");
		
		return rtn3;
	}
	
}
