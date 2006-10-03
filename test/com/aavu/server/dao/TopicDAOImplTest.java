package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.server.dao.db4o.TopicDAOdb4oImpl;

public class TopicDAOImplTest extends DAOTest {

	private TopicDAO tDAO;

	private User user = new User();
	
	protected void setUp() throws Exception {
		super.setUp();

		tDAO = (TopicDAO) new TopicDAOdb4oImpl();
		//tDAO.setDb(db);	
		
		user.setUsername("test");
		
	}



	public void testAll(){		

		
		
		
		Topic t = new Topic();
		t.setData("this is the text");
		t.setTitle("this is the title");
		tDAO.save(t);

		List<Topic> rtn = tDAO.getAllTopics(user);
		assertEquals(1,rtn.size());

		t = new Topic();
		t.setData("this is the text #2");
		t.setTitle("this is the title #2");
		tDAO.save(t);

		rtn = tDAO.getAllTopics(user);
		assertEquals(2,rtn.size());

		listResult(rtn);

	}

	public static void listResult(java.util.List result){
		System.out.println(result.size());
		for(int x = 0; x < result.size(); x++)
			System.out.println(result.get(x));
	}

	public void testTester(){
		tDAO.tester();
	}

}
