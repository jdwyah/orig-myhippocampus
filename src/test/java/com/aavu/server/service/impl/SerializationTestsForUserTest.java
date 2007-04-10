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
	
	public void testDeSerialize(){
		String s = "{EX}[23,5,-11,22,1170438315000,4,923,21,1170438315000,4,20,5,-11,0,1170438315000,4,924,19,1170438315000,4,18,-11,17,1170439922000,4,926,16,1170439922000,4,15,5,-11,14,1170439317000,4,925,13,1170439317000,4,12,5,11,0,10,6,1,9,8,1170441974000,4,927,7,1170441974000,4,6,5,7,3,400,400,1170443800294,4,0,3,1609";		
		s+=",1170438180000,4,0,3,2,1,1,[\"[Lcom.aavu.client.domain.Topic;/1245490978\",\"com.aavu.client.domain.Tag/1086466989\",\"java.util.HashSet/1594477813\",\"java.util.Date/1659716317\",\"com.aavu.client.domain.WebLink/1289949727\",\"http://www.nytimes.com\",\"nytimes\",\"link 4\",\"com.aavu.client.domain.User/3949081744\",\"7694f4a66316e53c8cdd9d9954bd611d\",\"q\",\"http://google.com\",\"second site\",\"GWT site 2\",\"http://www.www.www\",\"foo\",\"link 3\",\"com.aavu.client.domain.Entry/1386376975\",\"<BODY contentEditable=true></BODY>\",\"http://groups.google.com/group/Google-Web-Toolkit\",\"\",\"GWT forum\",\"http://l6.com\"],0,2]";
		
		 String s2 = "{OK}[-10,0,2,27,0,0,2,0,-10,26,1170443634000,3,928,25,1170443634000,3,24,4,-10,23,1170438315000,3,923,22,1170438315000,3,21,4,-10,0,1170438315000,3,9"+
		      "24,20,1170438315000,3,19,-10,18,1170439922000,3,926,17,1170439922000,3,16,4,-10,15,1170443800000,3,931,15,1170443800000,3,14,4,-10,13,1170439317000,3,"+
		      "925,12,1170439317000,3,11,4,10,0,9,6,1,8,7,1170441974000,3,927,6,1170441974000,3,5,4,7,2,400,400,1170444600060,3,0,2,1609,1170438180000,3,0,2,1,[\"com."+
		      "aavu.client.domain.Tag/1086466989\",\"java.util.HashSet/1594477813\",\"java.util.Date/1659716317\",\"com.aavu.client.domain.WebLink/1289949727\",\"http://www."+
		      "nytimes.com\",\"nytimes\",\"link 4\",\"com.aavu.client.domain.User/3949081744\",\"7694f4a66316e53c8cdd9d9954bd611d\",\"q\",\"http://google.com\",\"second site\",\"GWT"+
		      " site 2\",\"http://l6.com\",\"l6\",\"http://www.www.www\",\"foo\",\"link 3\",\"com.aavu.client.domain.Entry/1386376975\",\"<BODY contentEditable=true></BODY>\",\"http"+
		      "://groups.google.com/group/Google-Web-Toolkit\",\"\",\"GWT forum\",\"http://www.zefrank.org\",\"yoooooo\",\"link 5\",\"GWT\"],0,2]";
		 
		 //assertEquals(s, s2);
	}
	
	
	
	
	
	
	
}
