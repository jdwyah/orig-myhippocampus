package com.aavu.server.service.gwt;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
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
	private TimeLineObj[] convertToArray(List<TimeLineObj> list){

		TimeLineObj[] rtn = new TimeLineObj[list.size()];
		for(int i=0;i<list.size();i++){				
			TimeLineObj t = list.get(i);
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
		log.debug("CONVERT Topic");
		log.debug("Topic's tags: "+t.getId()+" "+t.getTitle()+" "+t.getParents().getClass());

			
		//didn't need to convert the postgres one, but mysql is
		//returning java.sql.timestamp, which, surprise surprise 
		//is another thing that breaks GWT serialization.
		//
		t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
		t.setCreated(new Date(t.getCreated().getTime()));
		
		log.debug("t "+t.getParents().getClass());		
		log.debug("t "+t.getMetaValues().getClass());

		log.debug("loop meta values");
		HashMap<Topic, Topic> convertedMap = new HashMap<Topic, Topic>();		
		for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
			Topic element = (Topic) iter.next();
			log.debug(element);
			convertedMap.put(element, (Topic) t.getMetaValues().get(element));
		}
		t.setMetaValues(convertedMap);
					
		log.debug("starting convert sets");
		//metavalues
		t.setParents(converter(t.getParents()));
		log.debug("A");
		t.setChildren(new HashSet());
		log.debug("B");
		t.setMetas(converter(t.getMetas()));
		log.debug("C");
		t.setOccurences(new HashSet());
		log.debug("D");
		t.setAssociations(new HashSet());
		log.debug("E");
		
		log.debug("t "+t.getMetaValues().getClass());	

		log.debug("Finally: t "+t.getId()+" "+t.getUser());
		return t;
	}

	public static Set converter(Set in){
		return converter(in,false);
	}
	public static Set converter(Set in,boolean nullIt){		
		HashSet<Topic> rtn = new HashSet<Topic>();
		log.debug("converter "+in+" "+rtn);
		for (Iterator iter = in.iterator(); iter.hasNext();) {
			Topic top = (Topic) iter.next();
			log.debug("converter on "+top);
			convert(top);
			log.debug("converted "+top);
			/*if(nullIt){
				top.setMetaValues(null);
				top.setParents(null);
				top.setChildren(null);
				top.setMetas(null);
				top.setOccurences(null);
				top.setAssociations(null);
				
			}else{
				
				top.setMetaValues(converter(top.getMetaValues(),true));
				top.setParents(converter(top.getParents(),true));
				top.setChildren(converter(top.getChildren(),true));
				top.setMetas(converter(top.getMetas(),true));
				top.setOccurences(converter(top.getOccurences(),true));
				top.setAssociations(converter(top.getAssociations(),true));
			}*/
			rtn.add(top);
		}
		
		return rtn;		
	}
	
	private Map converter(Map metaValues) {
		return converter(metaValues,false);
	}
	private static Map converter(Map metaValues, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public Topic getTopicForName(String topicName) {
		try {
			Topic t = topicService.getForName(topicName);
			log.debug("orig "+t.getId()+" "+t.getTitle());
			
			//just json the Abstract topic parts
			Topic converted = convert(t);
			log.debug("conv: "+t.getId()+" tit "+t.getTitle());
	
			return converted;
			
			//return convert(topicService.getForName(topicName));

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

	public Topic getTopicByID(long topicID) {
		try {

			return convert(topicService.getForID(topicID));

		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

	public List getTimelineObjs() {
		try {
			return topicService.getTimelineObjs();
			
		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}

}
