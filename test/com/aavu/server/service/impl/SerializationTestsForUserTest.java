package com.aavu.server.service.impl;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.gwt.BaseTestNoTransaction;
import com.aavu.server.service.gwt.Converter;

public class SerializationTestsForUserTest extends BaseTestNoTransaction {


	@Override
	protected String getPass() {
		return "test";
	}
	@Override
	protected String getUName() {
		return "test";
	}



	private TopicService topicService;

	
	
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}



	public void testSerializationOfMany(){
		
		User uu = new User();
		uu.setId(1);
		
		Topic t = topicService.getForID(515);
		
		String str = Converter.serializeWithHibernateSupport(t);
				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		t = topicService.getForID(707);		
		str = Converter.serializeWithHibernateSupport(t);				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		
		t = topicService.getForID(208);		
		str = Converter.serializeWithHibernateSupport(t);				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		t = topicService.getForID(970);		
		str = Converter.serializeWithHibernateSupport(t);				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		t = topicService.getForID(204);		
		str = Converter.serializeWithHibernateSupport(t);				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		t = new Topic();
		t.setId(1041);
		Object o = topicService.getLinksTo(t);
		
		str = Converter.serializeWithHibernateSupport(t);						
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
	}
	public void testSerializationOfSubTypes(){

		User uu = new User();
		uu.setId(1);

		Topic t = topicService.getForID(935);

		String str = Converter.serializeWithHibernateSupport(t);

		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		assertTrue(str.contains("MetaDate"));
		assertTrue(str.contains("MetaText"));
		assertTrue(str.contains("MetaTopic"));


	}
	
}
