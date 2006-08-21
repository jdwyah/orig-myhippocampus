package com.aavu.server.service.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaValue;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.server.service.TopicService;


public class GWTTopicServiceImpl extends GWTSpringController implements GWTTopicService {

	private TopicService topicService ;

	String setByDI = "UNSET";

	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}


	public void setSetByDI(String setByDI) {
		this.setByDI = setByDI;
	}


	/*
	 * @gwt.typeArgs <com.aavu.client.domain.TopicGWT>
	 */
	public Topic[] getAllTopics(int startIndex, int maxCount) {
		try {


			System.out.println("get all topics SETBY "+setByDI);

			return convertToArray(topicService.getAllTopics());
		} catch (Exception e) {
			System.out.println("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	public void save(Topic topic) {

		System.out.println("Save topics");

		topicService.save(topic);

	}


	public String[] match(String match) {
		System.out.println("match");
		List<Topic> list = (topicService.getTopicsStarting(match));

		String[] rtn = new String[list.size()];
		int i=0;
		for(Topic t : list){
			rtn[i++] = t.getTitle();
		}		
		return rtn;		
	}


//	public void save(Topic topic, String[] seeAlsos) {
//	System.out.println("Save topics w seealso");

//	topic.getSeeAlso().clear();

//	for (int i = 0; i < seeAlsos.length; i++) {


//	String string = seeAlsos[i];
//	if(!string.equals("")){


//	System.out.println("lookup |"+string+"|");
//	Topic also = topicService.getForName(string);

//	System.out.println("also "+also);

//	if(also != null){			
//	topic.getSeeAlso().add(also);
//	}
//	}
//	}

//	save(topic);


//	}


	public Topic[] getBlogTopics(int start, int numberPerScreen) {
		try{
			System.out.println("get Blog topics");
			return convertToArray(topicService.getBlogTopics(start,numberPerScreen));
		} catch (Exception e) {
			System.out.println("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	private Topic[] convertToArray(List<Topic> list){

		System.out.println("ConvertToArray");

		Topic[] rtn = new Topic[list.size()];

		for(int i=0;i<list.size();i++){				
			Topic t = list.get(i);

			System.out.println("t "+t.getTags().getClass());
			
			ArrayList<Tag> nTags = new ArrayList<Tag>();
			nTags.addAll(t.getTags());
			t.setTags(nTags);
			
			System.out.println("t "+t.getTags().getClass());
			
			System.out.println("t "+t.getMetaValues().getClass());
			
			HashMap<Meta, MetaValue> nMap = new HashMap<Meta, MetaValue>();
			nMap.putAll(t.getMetaValues());			
			t.setMetaValues(nMap);
			System.out.println("t "+t.getMetaValues().getClass());	
			
			
			System.out.println("t "+i+" "+t.getId()+" "+t.getUser());
			//t.setUser(null);
			rtn[i] = t;
		}

		return rtn;
	}

}
