package com.aavu.server.service.gwt;

import junit.framework.TestCase;

import com.aavu.client.domain.Topic;

public class ConverterTest extends BaseTestNoTransaction {
	
	
	public void testConvert(){
		
		Topic topic = new Topic();
		
		
		Topic converted = (Topic) Converter.convert(topic);
		
		assertFalse(Converter.scan(converted));
	}
}
