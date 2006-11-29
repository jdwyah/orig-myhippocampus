package com.aavu.server.lucene.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.compass.core.converter.ConversionException;

import com.aavu.server.lucene.util.html.HTMLParser;

public class HTMLAnalyzer extends StandardAnalyzer {
	private static final Logger log = Logger.getLogger(HTMLAnalyzer.class);
		
	@Override
	public TokenStream tokenStream(String name, Reader reader) {

		if(log.isDebugEnabled()){
			TokenStream ts = super.tokenStream(name, htmlReaderFromReader(reader));
			Token t;
//			if(log.isDebugEnabled()){
//				try {
//					while((t = ts.next()) != null){
//						log.debug("token: "+t);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
		
		return super.tokenStream(name,htmlReaderFromReader(reader));
	}

	private Reader htmlReaderFromReader(Reader reader) throws ConversionException {

		HTMLParser parser = new HTMLParser(reader);
		try {
			return parser.getReader();
		} catch (IOException e) {
			throw new ConversionException(e.getMessage());
		}
		
	}

}
