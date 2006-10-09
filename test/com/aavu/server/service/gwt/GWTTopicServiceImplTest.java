package com.aavu.server.service.gwt;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.service.UserService;


public class GWTTopicServiceImplTest extends BaseTestNoTransaction  {
	private static final Logger log = Logger.getLogger(GWTTopicServiceImplTest.class);
	
	private GWTTopicService topicService;
	private UserDAO userDAO;	
	private TopicDAO topicDAO;
	private UserService userService;

	private User u;
	
	private static final String B = "Author";
	private static final String C = "HuntForRedOctober";
	private static final String D = "Book";
	private static final String E = "TomClancy";
	

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}	
	public void setTopicService(GWTTopicService topicService) {
		this.topicService = topicService;
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	@Override
	protected void onSetUp() throws Exception {		
		super.onSetUp();

		u = userService.getCurrentUser();
		
		initDBTables();
	}


	private void initDBTables(){
		
		Topic huntForRedOctober = topicService.getTopicForName(C);

		if(huntForRedOctober == null){
			
			log.debug("INITING");

			Topic patriotGames = new Topic(u,C);
			patriotGames.setData(B);
			
			Tag book = new Tag(u,D);
			

			MetaTopic author = new MetaTopic();
			author.setTitle(B);
			author.setUser(u);
			
			Topic savedAuthor = topicDAO.save(author);
			
			book.addMeta(savedAuthor);

			Topic savedBook = topicDAO.save(book);

			Topic tomClancy = new Topic(u,E);
			
			Topic savedTomClancy = topicDAO.save(tomClancy);

//			/Topic savedAuthor = (Topic) savedBook.getMetas().iterator().next();
			
			patriotGames.tagTopic(savedBook);
			patriotGames.addMetaValue(savedAuthor, savedTomClancy);

			System.out.println("before: "+patriotGames.getId());

			System.out.println(patriotGames.toPrettyString());
			
			topicDAO.save(patriotGames);
		}	else{
			log.debug("NO INIT");
		}
	}


	public void testGetATopic() {
		
		Topic huntForRedOctober = topicService.getTopicForName(C);
		
		assertNotNull(huntForRedOctober);
		
		assertEquals(1, huntForRedOctober.getTypes().size());
		
		Tag book = (Tag) huntForRedOctober.getTypes().iterator().next();
		
		assertEquals(D, book.getTitle());
		
		//this is the test to make work
		assertEquals(1, book.getMetas().size());
		
		Meta author = (Meta) book.getMetas().iterator().next();
		
		assertEquals(B, author.getTitle());
		
		
		Topic tomC = (Topic) huntForRedOctober.getMetaValues().get(author);
		assertEquals(E, tomC.getTitle());
		
		
		//Interesting to note:
		//Didn't Automatically Load a types->instances
		assertEquals(0, book.getInstances().size());
		
		//but it is really in there
		Topic book2 = topicService.getTopicForName(D);
		assertEquals(1, book2.getInstances().size());
		
		//and another check
		assertEquals(1,topicService.getTopicIdsWithTag(book).length);
		
		
	}

}
