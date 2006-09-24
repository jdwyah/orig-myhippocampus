package com.aavu.server.service.gwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
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
//				ok(topic.getMetaValues());

//				for (Iterator iter = topic.getMetaValues().keySet().iterator(); iter.hasNext();) {
//				Meta element = (Meta) iter.next();
//				log.debug("--------------------");
//				ok(element);
//				log.debug(element.getName());
//				log.debug(element.getClass());
//				topic.getMetaValues().put(element, topic.getMetaValues().get(element));					
//				log.debug("--------------------");
//				ok(topic.getMetaValues().get(element));
//				}
//				for (Iterator iter = topic.getMetaValues().keySet().iterator(); iter.hasNext();) {
//				Meta element = (Meta) iter.next();
//				log.debug("-------2222---------");
//				ok(element);
//				log.debug(element.getName());
//				log.debug(element.getClass());					
//				log.debug("-------2222------");
//				ok(topic.getMetaValues().get(element));
//				}
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

	public Topic save(Topic topic) {

		try {
			log.debug("Save topics");
			log.debug(topic.toPrettyString());

			return convert(topicService.save(topic));

		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}


	public Topic[] match(String match) {
		log.debug("match");
		try {
			List<Topic> list = (topicService.getTopicsStarting(match));
			return convertToArray(list);

		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
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
	private TopicIdentifier[] convertToArray(List<TopicIdentifier> list){

		TopicIdentifier[] rtn = new TopicIdentifier[list.size()];
		for(int i=0;i<list.size();i++){				
			TopicIdentifier t = list.get(i);
			rtn[i] = t;
		}
		return rtn;
	}

	/**
	 * Convert a topic to GWT serializable form. 
	 * 
	 * This means;
	 * 1) Change org.hibernate.collection.PersistentList to java.util.ArrayList
	 *    -because GWT can't handle 
	 * 2) Initialize an "proxy" objects. We can't have any lazy references! Even if we won't
	 *    peek on the client. We can't transfer these it seems. 
	 * 
	 * @param t
	 * @return
	 */
	public static Topic convert(Topic t){
		log.debug("Topic's tags: "+t.getId()+" "+t.getTitle()+" "+t.getTags().getClass());

//		if(9==9){
//			Converter c = new Converter();
//			return (Topic) c.convert(t);
//		}
		
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

		//didn't need to convert the postgres one, but mysql is
		//returning java.sql.timestamp, which, surprise surprise 
		//is another thing that breaks GWT serialization.
		//
		t.setLastUpdated(new Date(t.getLastUpdated().getTime()));

		log.debug("t "+t.getTags().getClass());		
		log.debug("t "+t.getMetaValueStrs().getClass());

		log.debug("loop metas");
//		HashMap<Meta, MetaValue> nMap = new HashMap<Meta, MetaValue>();

		HashMap<String, String> nMap = new HashMap<String, String>();

		//nMap.putAll(t.getMetaValues());					

//		for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
//		Meta element = (Meta) iter.next();
//		log.debug(element.getName());
//		nMap.put(element, (String) t.getMetaValues().get(element));
//		}
		for (Iterator iter = t.getMetaValueStrs().keySet().iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			log.debug(element);
			nMap.put(element, (String) t.getMetaValueStrs().get(element));
		}

		t.setMetaValueStrs(nMap);
		log.debug("t "+t.getMetaValueStrs().getClass());	

		log.debug("Finally: t "+t.getId()+" "+t.getUser());
		return t;
	}

	public Topic getTopicForName(String topicName) {
		try {

			return convert(topicService.getForName(topicName));

		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * this conversion is just list -> array
	 * 
	 */
	public TopicIdentifier[] getTopicIdsWithTag(Tag tag) {
		try {

			return convertToArray(topicService.getTopicIdsWithTag(tag));

		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * this conversion is just list -> array
	 * 
	 */
	public TopicIdentifier[] getAllTopicIdentifiers() {
		try {
			return convertToArray(topicService.getAllTopicIdentifiers());
		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

}
