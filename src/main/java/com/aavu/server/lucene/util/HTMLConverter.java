package com.aavu.server.lucene.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.document.Field;
import org.compass.core.converter.ConversionException;
import org.compass.core.converter.basic.AbstractBasicConverter;
import org.compass.core.mapping.ResourcePropertyMapping;

import com.aavu.server.lucene.util.html.HTMLParser;

public class HTMLConverter extends AbstractBasicConverter {

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

	@Override
	public String toString(Object o, ResourcePropertyMapping resourcePropertyMapping) {
		StringReader reader = new StringReader((String) o);
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
