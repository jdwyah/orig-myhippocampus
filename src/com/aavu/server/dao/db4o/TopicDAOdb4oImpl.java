package com.aavu.server.dao.db4o;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.db4ospring.support.Db4oDaoSupport;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.domain.ServerSideUser;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class TopicDAOdb4oImpl extends Db4oDaoSupport implements TopicDAO{
	private static final Logger log = Logger.getLogger(TopicDAOdb4oImpl.class);	

	public void save(Topic t){


		if(t.getId() == 0){
			getDb4oTemplate().set(t);
			long id = getDb4oTemplate().getID(t);
			System.out.println("was 0, now "+id);				
			t.setId(id);							
		}else{
			getDb4oTemplate().bind(t, t.getId());			
		}

		getDb4oTemplate().delete(t.getLastUpdated());
		t.setLastUpdated(new Date());
		getDb4oTemplate().set(t);

	}

	public List<Topic> getAllTopics(final ServerSideUser user){

		log.debug("ABOUT TO GET ALL TOPICS");

		List<Topic> rtn = getDb4oTemplate().query(new Predicate<Topic>() {
			public boolean match(Topic topic) {
				System.out.println("Match "+topic);
				System.out.println("MatchID "+topic.getId());
				System.out.println("User "+user);
				System.out.println("TopicUser "+topic.getUser());
				if(null == topic.getUser()){
					log.debug("Null user for topic "+topic.getId()+" "+topic.getTitle());
					log.debug("SETTING USER");
					topic.setUser(user);
					getDb4oTemplate().set(topic);
					return false;
				}
				return topic.getUser().equals(user);
			}
		});

		log.debug("found "+rtn.size()+" topics");

		return rtn;

	}

	public List<Topic>  getTopicsStarting(final ServerSideUser user,final String match) {

		List<Topic> rtn = getDb4oTemplate().query(new Predicate<Topic>() {
			public boolean match(Topic topic) {			
				if(null == topic.getUser()){
					log.debug("Null user for topic "+topic.getId()+" "+topic.getTitle());
					return false;
				}
				return topic.getUser().equals(user) && topic.getTitle().toLowerCase().startsWith(match.toLowerCase());
			}
		});

		log.debug("found "+rtn.size()+" topics");		
		return rtn;
	}

	public Topic getForName(final ServerSideUser user,final String title) {

		Topic rtn = (Topic) Db4oUtil.getUniqueRes(getDb4oTemplate().query(new Predicate<Topic>() {
			public boolean match(Topic topic) {			
				if(null == topic.getUser()){
					log.debug("Null user for topic "+topic.getId()+" "+topic.getTitle());
					return false;
				}
				return topic.getUser().equals(user) && topic.getTitle().equals(title);
			}
		}));

		log.debug("found "+rtn);		
		return rtn;		
	}

	/**
	 *
	 * An interesting Db4o call. Native query, then sort. 
	 * http://developer.db4o.com/forums/thread/14720.aspx
	 * 
	 */
	public List<Topic> getBlogTopics(final ServerSideUser user,int start, int numberPerScreen) {

		log.debug("get BLOG TOPICS");

		//Get all topics
		//
		List<Topic> db4rtn = getDb4oTemplate().query(new Predicate<Topic>() {
			public boolean match(Topic topic) {	
				return topic.getUser().equals(user);
			}
		});

		
		//This is required because otherwise the list is implemented by ObjectSet, which when sorted, throws
		//UnsupportedOperationException - if the specified list's list-iterator does not support the set operation.
		//
		List<Topic> rtn = new LinkedList<Topic>();
		rtn.addAll(db4rtn);		

		
		//Then sort
		//
		Collections.sort(rtn,new Comparator<Topic>(){
			public int compare(Topic o1, Topic o2) {
				return o2.getLastUpdated().compareTo(o1.getLastUpdated());	
			}});

		log.debug("found "+rtn.size()+" topics");		
		return rtn;		
	}

	public void tester() {
		List<Tag> list = getDb4oTemplate().get(Tag.class);
		for(Tag t : list){
			System.out.println("name: "+t.getName()+" "+t.getId());
			System.out.println("meta: "+t.getMetas());

			if(t.getMetas() != null)
				for (Iterator iter = t.getMetas().iterator(); iter.hasNext();) {
					Meta element = (Meta) iter.next();
					System.out.println("meta "+element.getName()+" "+element.getType()+" "+element.getId());
				}

//			t.setMetas(new ArrayList());
//			if(t.getName() == null){
//			getDb().delete(t);				
//			}
//			saveT(t);
			//getDb().set(t);

		}
		List<Topic> list2 = getDb4oTemplate().get(Topic.class);
		for(Topic t : list2){

//			t.setLastUpdated(new Date());
//			getDb().set(t);
		}

	}
//	public void saveT(Tag tag) {

//	System.out.println("Tag: " + tag.getName());
//	System.out.println("metas: ");
//	for (Object meta : tag.getMetas()){
//	Meta metaCast = (Meta)meta;
//	System.out.println(metaCast);
//	}

//	Db4o.configure().objectClass("com.aavu.client.domain.Tag").cascadeOnUpdate(true);
//	Db4o.configure().objectClass("com.aavu.client.domain.Tag").updateDepth(1);

//	if(tag.getId() == 0){
//	getDb().set(tag);
//	long id = getDb().ext().getID(tag);
//	System.out.println("tag save was 0, now "+id);				
//	tag.setId(id);
//	getDb().set(tag);			
//	}else{
//	getDb().ext().bind(tag, tag.getId());
//	getDb().set(tag);
//	}
//	closeDB();

//	}

}
