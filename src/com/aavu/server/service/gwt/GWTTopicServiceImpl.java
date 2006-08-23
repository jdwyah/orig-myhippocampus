package com.aavu.server.service.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaValue;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.server.service.TopicService;


public class GWTTopicServiceImpl extends GWTSpringController implements GWTTopicService {
	
	private static final Logger log = Logger.getLogger(GWTTopicServiceImpl.class);
	
	private TopicService topicService ;

	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	public Topic[] getAllTopics(int startIndex, int maxCount) {
		try {

			Topic[] rtn = convertToArray(topicService.getAllTopics());
			for (int i = 0; i < rtn.length; i++) {
				Topic topic = rtn[i];
				log.debug("rtn");
				ok(topic);
				ok(topic.getLastUpdated());
				ok(topic.getMetaValues());
				
				for (Iterator iter = topic.getMetaValues().keySet().iterator(); iter.hasNext();) {
					Meta element = (Meta) iter.next();
					ok(element);
					ok(topic.getMetaValues().get(element));
				}
				for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
					Tag element = (Tag) iter.next();
					if(element != null){
						ok(element.getMetas());
					}
				}
				ok(topic.getTags());
				ok(topic.getUser());
			}
			return rtn;
			//return convertToArray(topicService.getAllTopics());
		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}
	private void ok(Object o){
		if(o != null){
			log.debug("rtn class ok "+o.getClass());
		}
	}

	public void save(Topic topic) {

		log.debug("Save topics");
		log.debug(topic.toPrettyString());

		topicService.save(topic);

	}


	public String[] match(String match) {
		log.debug("match");
		List<Topic> list = (topicService.getTopicsStarting(match));

		String[] rtn = new String[list.size()];
		int i=0;
		for(Topic t : list){
			rtn[i++] = t.getTitle();
		}		
		return rtn;		
	}


//	public void save(Topic topic, String[] seeAlsos) {
//	log.debug("Save topics w seealso");

//	topic.getSeeAlso().clear();

//	for (int i = 0; i < seeAlsos.length; i++) {


//	String string = seeAlsos[i];
//	if(!string.equals("")){


//	log.debug("lookup |"+string+"|");
//	Topic also = topicService.getForName(string);

//	log.debug("also "+also);

//	if(also != null){			
//	topic.getSeeAlso().add(also);
//	}
//	}
//	}

//	save(topic);


//	}


	public Topic[] getBlogTopics(int start, int numberPerScreen) {
		try{
			log.debug("get Blog topics");
			return convertToArray(topicService.getBlogTopics(start,numberPerScreen));
		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	private Topic[] convertToArray(List<Topic> list){

		log.debug("ConvertToArray");

		Topic[] rtn = new Topic[list.size()];

		for(int i=0;i<list.size();i++){				
			Topic t = list.get(i);

			
			//t.setUser(null);
			rtn[i] = convert(t);
		}

		return rtn;
	}

	public static Topic convert(Topic t){
		log.debug("Topic's tags: "+t.getId()+" "+t.getTitle()+" "+t.getTags().getClass());
		
		ArrayList<Tag> nTags = new ArrayList<Tag>();

		for (Iterator iter = t.getTags().iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			log.debug("tagToConvert: "+tag);
			if(tag != null){
				log.debug("name: "+tag.getName());
				GWTTagServiceImpl.convert(tag);
			}
		}
		
		nTags.addAll(t.getTags());
		t.setTags(nTags);
		
		log.debug("t "+t.getTags().getClass());		
		log.debug("t "+t.getMetaValues().getClass());
		
		HashMap<Meta, MetaValue> nMap = new HashMap<Meta, MetaValue>();
		nMap.putAll(t.getMetaValues());			
		t.setMetaValues(nMap);
		log.debug("t "+t.getMetaValues().getClass());	
				
		log.debug("t "+t.getId()+" "+t.getUser());
		return t;
	}
}
