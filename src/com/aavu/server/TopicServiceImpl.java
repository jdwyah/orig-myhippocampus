package com.aavu.server;

import java.util.List;

import com.aavu.client.TopicService;
import com.aavu.client.domain.Topic;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.db4o.TopicDAOdb4oImpl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class TopicServiceImpl extends RemoteServiceServlet implements TopicService {


	private TopicDAO topicDAO ;

	public TopicServiceImpl(){
		System.out.println("CTOR");


		TopicDAOdb4oImpl tDAO = new TopicDAOdb4oImpl();
		setTopicDAO(tDAO);
	}


	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}

	/*
	 * @gwt.typeArgs <com.aavu.client.domain.TopicGWT>
	 */
	public Topic[] getAllTopics(int startIndex, int maxCount) {
		System.out.println("here!!");

//		Topic t = new Topic();
//		t.setTitle("tile");
//		Topic[] r = new Topic[1];
//		r[0] = t;
//		return r;


		//try{

			List<Topic> list = topicDAO.getAllTopics();

			//Topic[] rtn = new Topic[list.size()+1];
//			Topic tt = new Topic();
//			tt.setTitle("Title: "+yac);
//			rtn[rtn.length-1] = tt;

			Topic[] rtn = new Topic[list.size()];

			for(int i=0;i<list.size();i++){				
				rtn[i] = list.get(i);
			}
			

			return rtn;
//
//		}catch(Exception e){
//			Topic t = new Topic();
//			
//			t.setTitle("Error! "+e);			
//			e.printStackTrace();
//			
//			Topic[] r = new Topic[1];
//			r[0] = t;
//			return r;
//		}

	}

	public void save(Topic topic) {
		
		topicDAO.save(topic);

	}


	public String[] match(String match) {

		List<Topic> list = (topicDAO.getTopicsStarting(match));
		
		String[] rtn = new String[list.size()];
		int i=0;
		for(Topic t : list){
			rtn[i++] = t.getTitle();
		}		
		return rtn;		
	}
	
}
