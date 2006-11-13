package com.aavu.server.lucene.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.compass.core.converter.ConversionException;
import org.compass.core.converter.basic.AbstractBasicConverter;
import org.compass.core.mapping.ResourcePropertyMapping;

import com.aavu.server.lucene.util.html.HTMLParser;

public class HTMLAnalyzer extends StandardAnalyzer {

	
	
	@Override
	public TokenStream tokenStream(String arg0, Reader arg1) {
		// TODO Auto-generated method stub
		return super.tokenStream(arg0, arg1);
	}

	public Object fromString(String htmlString, ResourcePropertyMapping arg1)
	throws ConversionException {

		StringReader reader = new StringReader(htmlString);
		HTMLParser parser = new HTMLParser(reader);

		StringBuilder sb = new StringBuilder();
		String thisLine;
		try {
			BufferedReader parsedReader = new BufferedReader(parser.getReader());
			while ((thisLine = parsedReader.readLine()) != null) { // while loop begins here
				sb.append(thisLine);
			} // end while 
			return sb.toString();			
		} catch (IOException e) {
			throw new ConversionException(e.getMessage());
		}
	}

}
