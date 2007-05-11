package com.aavu.server.service.gwt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
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

	/**
	 * Implement a test HSQLDB 
	 *
	 */
	private void initDBTables(){
		
		//topicDAO.deleteAllTables();
			
	}

	private void doSomeInit() throws HippoException{

		log.debug("INITING");

		Topic patriotGames = new Topic(u,C);
		patriotGames.getLatestEntry().setData(B);
		
		Tag book = new Tag(u,D);
		

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);
		
		Meta savedAuthor = (Meta) topicDAO.save(author);
		
		book.addTagProperty(savedAuthor);

		Topic savedBook = topicDAO.save(book);

		Topic tomClancy = new Topic(u,E);
		
		Topic savedTomClancy = topicDAO.save(tomClancy);

//		/Topic savedAuthor = (Topic) savedBook.getMetas().iterator().next();
		
		patriotGames.tagTopic(savedBook);
		patriotGames.addMetaValue(savedAuthor, savedTomClancy);

		System.out.println("before: "+patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());
				
		//topicService.save(patriotGames);
		//topicService.save(savedBook);
	}

//	public void testGetATopic() throws HippoException {
//
//		doSomeInit();
//		
//		Topic huntForRedOctober = topicService.getTopicForName(C);
//		
//		assertNotNull(huntForRedOctober);
//		
//		assertEquals(1, huntForRedOctober.getTypesAsTopics().size());
//		
//		Tag book = (Tag) huntForRedOctober.getTypesAsTopics().iterator().next();
//		
//		assertEquals(D, book.getTitle());
//		
//		System.out.println("----------------------------");
////		Topic sb = topicService.getTopicByID(book.getId());
////		
////		assertEquals(1, sb.getMetas().size());
//		
//		System.out.println(huntForRedOctober.toPrettyString());
//		
//		//this is the test to make work
//		assertEquals(1, book.getTagProperties().size());
//		
//		Meta author = (Meta) book.getTagProperties().iterator().next();
//		
//		assertEquals(B, author.getTitle());
//		
//		
//		Topic tomC = (Topic) huntForRedOctober.getSingleMetaValueFor(author);
//		assertEquals(E, tomC.getTitle());
//		
//		
//		//Interesting to note:
//		//Didn't Automatically Load a types->instances
//		//assertEquals(0, book.getInstances().size());
//		
//		//but it is really in there
//		Topic book2 = topicService.getTopicForName(D);
//		//assertEquals(1, book2.getInstances().size());
//		
//		//and another check
//		//assertEquals(1,topicService.getTopicIdsWithTag(book.getId()).length);
//		
//		
//	}
	public void testDisappearingTagInstances() throws HippoBusinessException{
		
		Topic patriotGames = new Topic(u,C);
		patriotGames.getLatestEntry().setData(B);
				
		Tag book = new Tag(u,D);
						
		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);
		
		book.addTagProperty(author);
				
		book = (Tag) topicDAO.save(book);
		
		Topic tomClancy = new Topic(u,E);
		topicDAO.save(tomClancy);
		
		System.out.println("book: "+book);
		//System.out.println("book "+book.getInstances());
		
		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);
		
		System.out.println("before: "+patriotGames.getId());
		
		topicDAO.save(patriotGames);
		topicDAO.save(book);
		
		
		Topic savePatriot = topicService.getTopicForName(C);
		Topic savedBook = (Topic) savePatriot.getTags().iterator().next();

		
		//System.out.println("before clear "+book.getInstances().size());
		
		//replicate the GWT converting NULL;
		//savedBook.setInstances(new HashSet());
		
		Topic b = (Topic) savePatriot.getTags().iterator().next();
		//assertEquals(0, b.getInstances().size());
		
		Topic save2 = topicDAO.save(savePatriot);
		
		Topic savedBook3 = topicDAO.save(savedBook);
		
		//System.out.println("book "+book.getId()+" i.sz "+book.getInstances().size()+" "+" sb "+savedBook.getId()+" ");
		
		//this is 0. We aren't going to load these.
		Topic sb = (Topic) save2.getTags().iterator().next();
		//assertEquals(0, sb.getInstances().size());
		
		//this is the important one
		//assertEquals(1, savedBook3.getInstances().size());
	
		Topic reloadedBook = topicService.getTopicForName(D);
		//assertEquals(1, reloadedBook.getInstances().size());
			
	}
	
	public void testTimeline() throws HippoException{
		
		List<TopicIdentifier> shoppingList = new ArrayList<TopicIdentifier>();
		shoppingList.add(new TopicIdentifier(35,"foo"));
		
		
		List<List<FullTopicIdentifier>> rtn = topicService.getTimelineWithTags(shoppingList);
		
		for(List<FullTopicIdentifier> ftis : rtn){
			for (FullTopicIdentifier identifier : ftis) {
				log.debug(identifier);
			}
		}				
		
		assertNotNull(rtn);
	}
	 
	
}
