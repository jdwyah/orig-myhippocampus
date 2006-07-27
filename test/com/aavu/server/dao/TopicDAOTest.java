package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.server.dao.db4o.TopicDAOdb4oImpl;

public class TopicDAOTest extends DAOTestCase {

	private TopicDAO tDAO;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		tDAO = new TopicDAOdb4oImpl();
		//tDAO.setDb(db);
	}
	

	
	public void testAll(){		

		Topic t = new Topic();
		t.setText("this is the text");
		t.setTitle("this is the title");
		tDAO.save(t);

		List<Topic> rtn = tDAO.getAllTopics();
		assertEquals(1,rtn.size());

		t = new Topic();
		t.setText("this is the text #2");
		t.setTitle("this is the title #2");
		tDAO.save(t);

		rtn = tDAO.getAllTopics();
		assertEquals(2,rtn.size());

		listResult(rtn);

	}
	
	public static void listResult(java.util.List result){
		System.out.println(result.size());
		for(int x = 0; x < result.size(); x++)
			System.out.println(result.get(x));
	}


}
