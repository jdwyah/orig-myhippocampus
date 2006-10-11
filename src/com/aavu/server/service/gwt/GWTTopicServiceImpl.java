package com.aavu.server.service.gwt;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.gwtwidgets.server.rpc.GWTSpringController;
import org.hibernate.LazyInitializationException;

import com.aavu.client.domain.Association;
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


	public String[] match(String match) {
		log.debug("match");
		try {
			
			String[] list = new String[]{};
			list = topicService.getTopicsStarting(match).toArray(list);
			
			return list;

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
		return convert(t,0);	
	}
	public static Topic convert(Topic t,int level){
		return convert(t,level,false);
	}
	public static Topic convert(Topic t,int level,boolean hasMembers){	
		log.debug("CONVERT Topic "+t+" level: "+level+"  members "+hasMembers);
		log.debug("Topic : "+t.getId()+" "+t.getTitle()+" tags:"+t.getTypes().getClass());
			
	
		
		log.debug("t "+t.getTypes().getClass());		
		log.debug("t "+t.getMetaValues().getClass());
	
		//
		//new-ing it is essentially nulling it out, since we can't pass
		//lazy stuff
		//
		//L2 new it out
		//L1 new everything expect metas we need this for topic's->tag's->metas
		//
		if(level >= 2){			
			t.setLastUpdated(null);
			t.setCreated(null);
			
			t.setMetaValues(new HashMap());
			t.setTypes(new HashSet());			
			t.setInstances(new HashSet());			
			t.setMetas(new HashSet());			
			t.setOccurences(new HashSet());			
			t.setAssociations(new HashSet());
		}else if(level == 1){
			//didn't need to convert the postgres one, but mysql is
			//returning java.sql.timestamp, which, surprise surprise 
			//is another thing that breaks GWT serialization.
			//
			t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
			t.setCreated(new Date(t.getCreated().getTime()));
			
			t.setMetaValues(new HashMap());
			t.setTypes(new HashSet());			
			t.setInstances(new HashSet());			
			t.setMetas(converter(t.getMetas(),level));			
			t.setOccurences(new HashSet());			
			t.setAssociations(new HashSet());
			
			log.debug("has Members "+hasMembers);
			//convert associations
			if(hasMembers){
				Association ass = (Association) t;			
				HashMap<String, Topic> convertedMap = new HashMap<String, Topic>();		
				for (Iterator iter = ass.getMembers().entrySet().iterator(); iter.hasNext();) {
					//
					//interestingly, looping over keys, then doing a map.get(key) returns us a null value.
					//something broken in our .equals()? or do I not understand Maps?
					//
					Entry e = (Entry) iter.next();
					String element = (String) e.getKey();
					Topic value = (Topic) e.getValue();

					log.debug("map "+element+"->"+value);
					convertedMap.put(element, convert(value,Integer.MAX_VALUE));
				}
				ass.setMembers(convertedMap);				
			}
			
		}else{
			//didn't need to convert the postgres one, but mysql is
			//returning java.sql.timestamp, which, surprise surprise 
			//is another thing that breaks GWT serialization.
			//
			t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
			t.setCreated(new Date(t.getCreated().getTime()));
			
			log.debug("loop meta values");
			HashMap<Topic, Topic> convertedMap = new HashMap<Topic, Topic>();		
			for (Iterator iter = t.getMetaValues().entrySet().iterator(); iter.hasNext();) {
				//
				//interestingly, looping over keys, then doing a map.get(key) returns us a null value.
				//something broken in our .equals()? or do I not understand Maps?
				//
				Entry e = (Entry) iter.next();
				Topic element = (Topic) e.getKey();
				Topic value = (Topic) e.getValue();
				
				log.debug("map "+element+"->"+value);
				convertedMap.put(convert(element,Integer.MAX_VALUE), convert(value,Integer.MAX_VALUE));
			}
			t.setMetaValues(convertedMap);

			log.debug("starting convert sets");
			
			//metavalues
			t.setTypes(converter(t.getTypes(),level));
			t.setInstances(converter(t.getInstances(),level));
			t.setMetas(converter(t.getMetas(),level));
			t.setOccurences(converter(t.getOccurences(),level));
			t.setAssociations(converter(t.getAssociations(),level,true));
			
			log.debug("t "+t.getMetaValues().getClass());	
		}
		log.debug("Finally: t "+t.getId()+" "+t.getUser());
		return t;
	}
	
	public static Set converter(Set in,int level){
		return converter(in, level,false);
	}
	public static Set converter(Set in,int level,boolean hasMembers){		
		HashSet<Topic> rtn = new HashSet<Topic>();
		try{
			log.debug("converter "+in+" "+rtn+" level: "+level);
			for (Iterator iter = in.iterator(); iter.hasNext();) {
				Topic top = (Topic) iter.next();
				log.debug("converter on "+top);
				convert(top,level+1,hasMembers);
				log.debug("converted "+top);

				rtn.add(top);
			}
		}catch(LazyInitializationException ex){
			log.debug("caught lazy @ level "+level);
		}
		return rtn;		
	}
	
//	private Map converter(Map metaValues) {
//		return converter(metaValues,false);
//	}
//	private static Map converter(Map metaValues, boolean b) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public Topic getTopicForName(String topicName) {
		try {
			Topic t = topicService.getForName(topicName);
			
			if(t != null){
				log.debug("orig "+t.getId()+" "+t.getTitle());

				//just json the Abstract topic parts
				Topic converted = convert(t);
				log.debug("conv: "+t.getId()+" tit "+t.getTitle());
				return converted;
			}else{
				log.debug("NULL");
			}
			
			return t;
			
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
