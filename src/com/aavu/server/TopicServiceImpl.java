package com.aavu.server;

import java.util.List;

import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.TopicService;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.db4o.TopicDAOdb4oImpl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class TopicServiceImpl extends GWTSpringController implements TopicService {

	private TopicDAO topicDAO ;

	String setByDI = "UNSET";
	
	public TopicServiceImpl(){
		System.out.println("Topic Service CTOR");

		TopicDAOdb4oImpl tDAO = new TopicDAOdb4oImpl();
		setTopicDAO(tDAO);
	}


	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}

	
	
	public void setSetByDI(String setByDI) {
		this.setByDI = setByDI;
	}


	/*
	 * @gwt.typeArgs <com.aavu.client.domain.TopicGWT>
	 */
	public Topic[] getAllTopics(int startIndex, int maxCount) {

		System.out.println("get all topics SETBY "+setByDI);
		
		return convertToArray(topicDAO.getAllTopics());
	}

	public void save(Topic topic) {

		System.out.println("Save topics");
		
		topicDAO.save(topic);

	}


	public String[] match(String match) {
		System.out.println("match");
		List<Topic> list = (topicDAO.getTopicsStarting(match));

		String[] rtn = new String[list.size()];
		int i=0;
		for(Topic t : list){
			rtn[i++] = t.getTitle();
		}		
		return rtn;		
	}


	public void save(Topic topic, String[] seeAlsos) {
		System.out.println("Save topics w seealso");
		
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


	public Topic[] getBlogTopics(int start, int numberPerScreen) {
		System.out.println("get Blog topics");
		return convertToArray(topicDAO.getBlogTopics(start,numberPerScreen));
		
	}
	
	private Topic[] convertToArray(List<Topic> list){

		Topic[] rtn = new Topic[list.size()];

		for(int i=0;i<list.size();i++){				
			rtn[i] = list.get(i);
		}

		return rtn;
	}

}
