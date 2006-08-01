package com.aavu.server;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.TopicService;
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

		List<Topic> list = topicDAO.getAllTopics();

		Topic[] rtn = new Topic[list.size()];

		for(int i=0;i<list.size();i++){				
			rtn[i] = list.get(i);
		}

		return rtn;
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


	public void save(Topic topic, String[] seeAlsos) {

		topic.getSeeAlso().clear();

		for (int i = 0; i < seeAlsos.length; i++) {


			String string = seeAlsos[i];
			if(!string.equals("")){


				System.out.println("lookup |"+string+"|");
				Topic also = topicDAO.getForName(string);

				System.out.println("also "+also);

				if(also != null){			
					topic.getSeeAlso().add(also);
				}
			}
		}

		save(topic);

	}

}
