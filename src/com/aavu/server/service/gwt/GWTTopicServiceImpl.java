package com.aavu.server.service.gwt;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoException;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.server.service.SearchService;
import com.aavu.server.service.TopicService;


public class GWTTopicServiceImpl extends org.gwtwidgets.server.spring.GWTSpringController implements GWTTopicService {

	private static final Logger log = Logger.getLogger(GWTTopicServiceImpl.class);

	private TopicService topicService;
	private SearchService searchService;

	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}


	public Topic save(Topic topic) throws HippoException {

		try {
			log.debug("Save topics");
			log.debug(topic.toPrettyString());

			return convert(topicService.save(topic));
		}  catch (HippoException ex) {
			log.error("Throw Hippo Exception: "+ex);
			throw ex;
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
		return convert(t,level,false,false);
	}
	public static Topic convert(Topic t,int level,boolean hasMembers,boolean typesWithAssociations){	
		log.debug("CONVERT Topic "+t+" level: "+level+"  members "+hasMembers);
		log.debug("Topic : "+t.getId()+" "+t.getTitle()+" tags:"+t.getTypes().getClass());



		log.debug("t "+t.getTypes().getClass());				

		//
		//new-ing it is essentially nulling it out, since we can't pass
		//lazy stuff
		//
		//L2 new it out
		//L1 new everything expect metas we need this for topic's->tag's->metas
		//
		if(level >= 2){	
			if(null == t){
				return t;
			}
			System.out.println("setting nul_____________________________________________");
			t.setLastUpdated(null);
			t.setCreated(null);
			t.setSubject(null);

			t.setScopes(new HashSet());			
			t.setInstances(new HashSet());						
			t.setOccurences(new HashSet());			
			t.setAssociations(new HashSet());

			if(hasMembers){		
				log.debug("LEVEL 2 HAS MEMBERS");

				Association ass = (Association) t;

				log.debug("types ");
				log.debug("size "+ass.getTypes().size());
				ass.setTypes(converter(ass.getTypes(), level));

				log.debug("members ");
				log.debug("size "+ass.getMembers().size());
				ass.setMembers(converter(ass.getMembers(), level));				
			}else{
				//is this necessary?
				//
				t.setTypes(new HashSet());				
			}

		}else if(level == 1){
			if(null == t){
				return t;
			}
			log.debug("l1 "+t);
			log.debug("last: "+t.getLastUpdated());
			log.debug("created: "+t.getCreated());

			//didn't need to convert the postgres one, but mysql is
			//returning java.sql.timestamp, which, surprise surprise 
			//is another thing that breaks GWT serialization.
			//
			//hmm, I think these get nulled in Level 2 set -> client,
			//then we error when the client passes this back. blech.
			//
			if(t.getLastUpdated() != null && t.getCreated() != null){
				t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
				t.setCreated(new Date(t.getCreated().getTime()));
			}


			t.setScopes(new HashSet());						
			t.setInstances(new HashSet());						
			t.setOccurences(new HashSet());	
			t.setSubject(null);

			log.debug("has Members "+hasMembers);

			if(typesWithAssociations){
				log.debug("LEVEL 1 typesWithAssociations");
				t.setAssociations(converter(t.getAssociations(), level,true));				
			}else{
				t.setAssociations(new HashSet());
			}

			//convert associations
			if(hasMembers){				
				Association ass = (Association) t;

				log.debug("types ");
				t.setTypes(converter(t.getTypes(), level));

				log.debug("members ");
				ass.setMembers(converter(ass.getMembers(), level));				
			}else{
				t.setTypes(new HashSet());
			}

		}else{
			//didn't need to convert the postgres one, but mysql is
			//returning java.sql.timestamp, which, surprise surprise 
			//is another thing that breaks GWT serialization.
			//
			log.debug("upd "+t.getLastUpdated());
			log.debug("cre "+t.getCreated());
			if(t.getLastUpdated() != null)
				t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
			if(t.getCreated() != null)
				t.setCreated(new Date(t.getCreated().getTime()));

			log.debug("starting convert sets");
			log.debug("SIZE: "+t.getTypes().size());
			t.setScopes(new HashSet());
			t.setTypes(converter(t.getTypes(),level,false,true));

			log.debug("starting convert sets-instances");
			t.setInstances(converter(t.getInstances(),level));

			log.debug("starting convert sets-occurrences");
			t.setOccurences(converterOccurenceSet(t.getOccurences()));

			log.debug("starting convert sets-assocations");
			t.setAssociations(converter(t.getAssociations(),level,true));

			if(t.getSubject() != null){
				t.getSubject().setTopics(new HashSet());
			}
		}
		log.debug("Finally: t "+t.getId()+" "+t.getUser());

		log.debug("Scan turned up persistent: "+Converter.scan(t));

		return t;
	}

	public static Set converter(Set in,int level){
		return converter(in, level,false);
	}
	public static Set converter(Set in,int level,boolean hasMembers){
		return converter(in,level,hasMembers,false);
	}
	public static Set converter(Set in,int level,boolean hasMembers,boolean typesWithAssociations){
		HashSet<Topic> rtn = new HashSet<Topic>();
		try{
			log.debug("converter "+in+" "+rtn+" level: "+level);
			for (Iterator iter = in.iterator(); iter.hasNext();) {
				Topic top = (Topic) iter.next();
				log.debug("converter on "+top);
				convert(top,level+1,hasMembers,typesWithAssociations);
				log.debug("converted "+top);

				rtn.add(top);
			}
		}catch(LazyInitializationException ex){
			log.debug("caught lazy @ level "+level);
		}
		return rtn;		
	}
	/**
	 * 1) Over-write dates
	 * 2) Null out the lazy loaded MindTree for MindTreeOcc's 
	 * 
	 * @param in
	 * @return
	 */
	public static Set converterOccurenceSet(Set in){
		HashSet<Occurrence> rtn = new HashSet<Occurrence>();
		try{			
			for (Iterator iter = in.iterator(); iter.hasNext();) {
				Occurrence top = (Occurrence) iter.next();		
				if(top.getLastUpdated() != null){
					top.setLastUpdated(new Date(top.getLastUpdated().getTime()));
				}
				if(top.getCreated() != null){
					top.setCreated(new Date(top.getCreated().getTime()));
				}				
				if (top instanceof MindTreeOcc) {
					MindTreeOcc mto = (MindTreeOcc) top;
					mto.setMindTree(null);
				}
				rtn.add(top);
			}
		}catch(LazyInitializationException ex){
			log.warn("caught lazy in convertOccurrence");
		}
		return rtn;		
	}

//	private Map converter(Map metaValues) {
//	return converter(metaValues,false);
//	}
//	private static Map converter(Map metaValues, boolean b) {
//	// TODO Auto-generated method stub
//	return null;
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


	public Topic[] save(Topic[] topics) throws HippoException {
		try {
			List<Topic> list = topicService.save(topics);
			Topic[] rtn = new Topic[list.size()];
			int i = 0;
			for (Topic topic : list) {
				log.debug("Converting "+topic.getId());
				rtn[i++] = convert(topic);
			}
			log.debug("Save[] rtn "+Arrays.toString(rtn));
			return rtn;
		}  catch (HippoException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			return null;
		}
	}


	public List getLinksTo(Topic topic) throws HippoException {
		try{
			return topicService.getLinksTo(topic);
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}


	public List search(String searchString) throws HippoException {
		try{
			return searchService.search(searchString);
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}
	public MindTree getTree(MindTreeOcc occ) throws HippoException {
		try{
			return convertTree(topicService.getTree(occ));
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}
	public MindTree saveTree(MindTree tree) throws HippoException {
		try{
			return  convertTree(topicService.saveTree(tree));
		}  catch (Exception e) {
			log.error("FAILURE: "+e);
			e.printStackTrace();
			throw new HippoException(e.getMessage());
		}
	}
	private MindTree convertTree(MindTree tree) {
		tree.setLeftSide(convertSetSimple(tree.getLeftSide()));
		tree.setRightSide(convertSetSimple(tree.getRightSide()));
		
		//TODO eek... this might be a bug -> bad things...
		tree.setTopic(null);
		
		return tree;
	}
	private Set convertSetSimple(Set in) {
		HashSet rtn = new HashSet();
		try{			
			for (Iterator iter = in.iterator(); iter.hasNext();) {				
				rtn.add(iter.next());
			}
		}catch(LazyInitializationException ex){
			log.debug("caught lazy ");
		}
		return rtn;		
	}

}
