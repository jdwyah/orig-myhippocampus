package com.aavu.server.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Subqueries;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.MetaLocation;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.util.SetUtils;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.web.domain.UserPageBean;

/**
 * 
 * @author Jeff Dwyer
 *
 */
public class TopicDAOHibernateImpl extends HibernateDaoSupport implements TopicDAO{
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImpl.class);
	private static final int DEFAULT_AUTOCOMPLET_MAX = 7;

	/**
	 * This is what will be fetched when you ask for a topic. If you follow the
	 * object graph beyond these bounds you'll be in lazy territory.
	 * 
	 * @param crit
	 * @return
	 */
	public static DetachedCriteria loadEmAll(DetachedCriteria crit){
		return crit.setFetchMode("user", FetchMode.JOIN)		
		//.setFetchMode("instances.topic", FetchMode.JOIN)
		.setFetchMode("subject", FetchMode.JOIN)				
		.setFetchMode("types", FetchMode.JOIN)
		.setFetchMode("types.type", FetchMode.JOIN)
		.setFetchMode("types.type.associations", FetchMode.JOIN)
		.setFetchMode("types.type.associations.types", FetchMode.JOIN)
		.setFetchMode("types.type.associations.members", FetchMode.JOIN)		
		.setFetchMode("occurences", FetchMode.JOIN)
		.setFetchMode("occurences.topics", FetchMode.JOIN)
		.setFetchMode("associations", FetchMode.JOIN)
		.setFetchMode("associations.members", FetchMode.JOIN)	
		.setFetchMode("associations.types", FetchMode.JOIN);
	}

	/**
	 * PEND MED a bit fragile. Note the capitalization. Is there another way to do this?
	 * 
	 * @param t
	 * @param toIsland
	 */
	public void changeState(final Topic t,final boolean toIsland) {
		int res = (Integer) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				String newType = "Topic";
				if(toIsland){
					newType = "tag";
				}								
				String hqlUpdate = "update Topic set discriminator = :newType where topic_id = :id";				
				int updatedEntities = sess.createQuery( hqlUpdate )
				.setString("newType", newType)
				.setLong( "id", t.getId() )	                            
				.executeUpdate();				
				return updatedEntities;				
			}});
		log.debug("res: "+res+" Changed t:"+t.getId()+" Now an island "+toIsland);
		
		getHibernateTemplate().evict(t);
		
		
	}
	
	/**
	 * 
	 */
	public void delete(final Topic todelete) {
		
	log.info("Deleting: "+todelete +" "+todelete.getId());
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {

				/*
				 * load-ing here prevents an error where todelete has unsaved occurences, which we then try to delete causing an "expected row 1"
				 * kind of error. If you want to delete todelete, there's no reason to save his associations first. 
				 * NOTE: we could just change this method to take the long topic_id instead of a real Topic.class
				 */				
				Topic topic  = (Topic) sess.get(Topic.class,todelete.getId());
				
				
				
//				log.info("Instances: "+topic.getInstances().size());
//				
//				for (TopicTypeConnector conn : (Set<TopicTypeConnector>)topic.getInstances()) {
//					getHibernateTemplate().delete(conn);
//				}
				
				/*
				 * delete all type/instance references to this topic
				 */
				List<TopicTypeConnector> conns = sess.createCriteria(TopicTypeConnector.class, "conn")
				.add(Expression.or((Expression.eq("conn.type", topic)),
				Expression.eq("conn.topic", topic))).list();
				
				
				System.out.println("found "+conns.size()+" connections.");
				for (TopicTypeConnector connector : conns) {
					connector.getTopic().getTypes().remove(connector);					
					System.out.println("Delete connection "+connector+" "+connector.getId()+" "+connector.getTopic().getId()+" to "+connector.getType().getId());
					sess.delete(connector);
				}				
				topic.getTypes().clear();
				
				System.out.println("after clear "+topic.toPrettyString());
				
//				for (TopicTypeConnector conn : (Set<TopicTypeConnector>)topic.getTypes()) {
//					log.info("Was a tag. Removing instances "+instance);
//					topic.getTypes().clear();					
//				}
				//topic.getInstances().clear();
//				
//				log.info("Types: "+topic.getTypes().size());
//				for (Topic type : (Set<Topic>)topic.getTypes()) {
//					log.info("Removing from type "+type);
//					type.getInstances().remove(topic);
//				}
//				topic.getTypes().clear();
				
				
				
				for (Occurrence occurence : (Set<Occurrence>)topic.getOccurences()) {
					
					//TODO delete S3Files 
					//TODO delete Weblinks that were only referenced by us					

					log.debug("remove occurrence: "+occurence.getId()+" "+occurence.getTitle()+" "+occurence.getData());

					if(occurence instanceof Entry){
						sess.delete(occurence);
					}
					if(occurence instanceof MindTreeOcc){
						sess.delete(occurence);
					}
				}
				topic.getOccurences().clear();
				
				
				/*
				 * Associations. 
				 * First delete my associations.
				 * Remember if Topic A has a seeAlso to Topic B
				 * A has an association with B as a member & B.associations.size == 0 
				 * So if we delete B, we need to go finding references in A. 
				 */
				topic.getAssociations().clear();
				for (Association assoc : (Set<Association>)topic.getAssociations()) {					
					sess.delete(assoc);					
				}
				
				/*
				 * next delete any references to me in other's associations. 
				 *  
				 */
				List<Association> associationsWithThisMember = sess.createCriteria(Association.class, "assoc")
				.createAlias("assoc.members", "mem")
				.add(Expression.eq("mem.id", topic.getId())).list();
				
				
				log.info("Associations With This Member: "+associationsWithThisMember.size());
				for(Association assoc : associationsWithThisMember){
					log.debug("removing "+topic+" from "+assoc);
					assoc.getMembers().remove(topic);
				}
				
				
				System.out.println("last gasp "+topic.toPrettyString());
				
				sess.delete(topic);				
				return true;
			}});
	}
	
	
	public void deleteOccurrence(final Occurrence toDelete) {
		
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
		
				Occurrence o  = (Occurrence) sess.get(Occurrence.class,toDelete.getId());
				
				log.debug("going to delete "+o+" "+o.getId()+" "+o.getData());
				
				Set<Topic> topics = o.getTopics();				
				for (Topic t : topics) {
					log.debug("remove "+t);

					boolean fnd = SetUtils.removeFromSetById(t.getOccurences(), o.getId());	
					
					if(!fnd){
						log.error("Problem removing occ "+o.getId()+" from "+t.getId());
					}
					
				}
				sess.delete(o);
				return null;
			}});
		
	}

	public Topic get(long topicID) {
		return (Topic) getHibernateTemplate().get(Topic.class, topicID);
	}
	

	public List getAllMetas(User user) {
		List<Object[]> ll2 = getHibernateTemplate().find("from Meta meta "+				
		"where meta.user = ?",user);
		return ll2;
	}
	
	
	private List<DatedTopicIdentifier> getAllTopicIdentifiers(DetachedCriteria crit) {
		
		List<Object[]> list = getHibernateTemplate().findByCriteria(crit);

		List<DatedTopicIdentifier> rtn = new ArrayList<DatedTopicIdentifier>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			//rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));
			rtn.add(new DatedTopicIdentifier((Long)o[1],(String)o[0],(Date)o[2],(Date)o[3]));
		}

		return rtn;
	}
	
	
	/**
	 * TODO replace hardcoded class discriminators with .class.
	 */	
	public List<DatedTopicIdentifier> getAllTopicIdentifiers(User user) {
		return getAllTopicIdentifiers(user, false);
	}

	/**
	 * all param is used by some unit tests to help wipe a user's account.
	 * 
	 */
	public List<DatedTopicIdentifier> getAllTopicIdentifiers(User user,boolean all) {

		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class);

		crit.add(Expression.eq("user", user))
		//never add seealso, this is the seeAlsoUber singleton
		.add(Expression.ne("class", "seealso"));
		
		if(!all){			
			crit.add(Expression.ne("class", "association"))			
			.add(Expression.ne("class", "metadate"))
			.add(Expression.ne("class", "metatext"))
			.add(Expression.ne("class", "metalocation"))
			.add(Expression.ne("class", "date"))
			.add(Expression.ne("class", "text"))
			.add(Expression.ne("class", "location"));
		}
		crit.addOrder( Order.asc("title").ignoreCase() )
		.setProjection(getTopicIdentifier());


		return getAllTopicIdentifiers(crit);
		
	}
	
	
	

	/**
	 * TESTING ONLY
	 * Should not be exposed to Service layer.
	 * 
	 * for when you really want the whole DB
	 * 
	 */
	public List<Topic> getAllTopics(User u) {		
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
				.add(Expression.eq("user", u)));
		return getHibernateTemplate().findByCriteria(crit);
	}

	public Topic getForID(User user, long topicID) {
		
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
				.add(Expression.eq("user", user))
				.add(Expression.eq("id", topicID)));

		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));			
	}
	

	public Topic getForName(User user, String string) {

		log.debug("user "+user.getUsername()+" string "+string);

		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
				.add(Expression.eq("user", user))
				.add(Expression.eq("title", string)));


		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));

		//return getHibernateTemplate().findByNamedParam("from Topic where user = :user and title = :title", "user", user);
	}
	public List<TopicIdentifier> getLinksTo(Topic topic,User user) {
		Object[] params = {topic.getId(),user};
		log.debug("----------getLinksTo-----------");
		log.debug("------------"+topic+"-------");
		
		/*
		 * Get Associations that mention this Topic
		 */
		List<Object[]> associationsToThis = getHibernateTemplate().find(""+
				"select title, id from Topic top "+		
				//"join top.associations "+
				"where ? in elements(top.associations.members) "+
				"and user is ? "
				,params);
		

		//associationsToThis.addAll(instancesOfThisTopicSlashTag);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(associationsToThis.size());
		for (Object[] o : associationsToThis){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}
		return rtn;		
	}
	
	
	
	public Occurrence getOccurrrence(long id) {
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Occurrence.class)				
				.add(Expression.eq("id", id)));

		return (Occurrence) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));				
	}

	public MetaSeeAlso getSeeAlsoSingleton() {
		return (MetaSeeAlso) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from MetaSeeAlso"));
	}

	/**
	 * 
	 * If you'd like the timeline for multiple tags, use the helper method in TopicService,
	 * which will just call this many times. I'm not sure if it's possible to do better than 
	 * that, since topics may have many tags and I haven't found a great way to query whether 
	 * something like topic.hasAnyOfItsTagsIn(Set tags) without iterating.
	 * 
	 * 
	 */
	public List<TimeLineObj> getTimeline(long tagID,User user) {

		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();

		//"where typeConn.type.class = MetaDate		
				
		//NOTE, aliases are INNER_JOINS so don't accidentally limt ourselves to only topics w/ tags
		DetachedCriteria crit = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.createAlias("associations", "assoc")				
		.createAlias("assoc.members", "metaValue")
		.createAlias("assoc.types", "assocTypeConn")
		.createAlias("assocTypeConn.type", "assocType")
		.add(Expression.eq("assocType.class", "metadate"));
		
		if(tagID != -1){
			DetachedCriteria tags = DetachedCriteria.forClass(TopicTypeConnector.class)
			.add(Expression.eq("type.id", tagID))
			.setProjection(Property.forName("topic.id"));

			log.debug("Using tag subquery");
			crit.add(Subqueries.propertyIn("id", tags));
		}
	
		crit.setProjection(Projections.projectionList()
				.add(Property.forName("id"))
				.add(Property.forName("title"))
				.add(Property.forName("metaValue.created")));
		
		List<Object[]>  ll = getHibernateTemplate().findByCriteria(crit);
		
		for (Object topic : ll) {
			Object[] oa = (Object[]) topic;
			for (int i = 0; i < oa.length; i++) {
				Object object = oa[i];
				if(object != null){
					System.out.println(" "+i+" "+object+" "+object.getClass());
				}else{
					System.out.println(" "+i+" "+object+" ");
				}
			}
			//?BigInteger topic_id = (BigInteger) oa[0];
			Long topicId = (Long) oa[0];
			
			Date date = (Date) oa[2];			

			//add metaDate
			rtn.add(new TimeLineObj(new TopicIdentifier(topicId.longValue(),(String)oa[1]),date,null));						
		}
		
		return rtn;
	}
	

	/**
	 * 
	 * 
	 */	
	public List<TimeLineObj> getTimeline(User user) {
		return getTimeline(-1,user);
	}

	/**
	 * NOTE: no user check. is that ok?
	 */
	public List<TopicIdentifier> getTopicForOccurrence(long id) {

		List<Object[]> list = getHibernateTemplate().find(""+
				"select title, id from Topic top "+		
				"where ? in elements(top.occurences) "						
				,id);
				
		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}

		return rtn;

	}

	
	/**
	 * Utility to set the projection properties for TopicIdentifier.
	 * @return
	 */
	private Projection getTopicIdentifier(){
		return Projections.projectionList()
		.add(Property.forName("title"))
		.add(Property.forName("id"))
		.add(Property.forName("created"))
		.add(Property.forName("lastUpdated"));
	}	
	public List<TopicTypeConnector> getTopicIdsWithTag(long tagid,User user) {
		
		List<TopicTypeConnector> rtn = getHibernateTemplate().find("from TopicTypeConnector conn "+
				"where conn.type.id = ? order by conn.topic.lastUpdated DESC",new Long(tagid));
				
//		if(log.isDebugEnabled()){
//			for (TopicTypeConnector connector : rtn) {
//				log.debug("DAO says "+connector.getId()+" "+connector.getLongitude()+" "+connector.getLatitude());
//			}
//		}
		
		return rtn;
	}

	/*
	 * TODO push this responsibility over to Compass, it should get better performance.
	 * 
	 * (non-Javadoc)
	 * @see com.aavu.server.dao.TopicDAO#getTopicsStarting(com.aavu.client.domain.User, java.lang.String)
	 */
	public List<String> getTopicsStarting(User user, String match) {
		return getTopicsStarting(user, match, DEFAULT_AUTOCOMPLET_MAX);		
	}

	public List<String> getTopicsStarting(User user, String match,int max) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)		
		.add(Expression.eq("user", user))
		.add(Expression.ilike("title", match, MatchMode.ANYWHERE))
		.add(Expression.ne("class", "association"))
		.add(Expression.ne("class", "seealso"))
		.add(Expression.ne("class", "metadate"))
		.add(Expression.ne("class", "text"))//this is correct, right?
		.add(Expression.ne("class", "date"))
		
		.addOrder( Order.asc("title") )
		
		.setProjection(Property.forName("title"));	

		return getHibernateTemplate().findByCriteria(crit,0,max);
	}

	/**
	 * Note don't sysout fullocc since it's User is not loaded and that LazyLEx's.
	 * 
	 */
	public MindTree getTree(MindTreeOcc occ) {		

		DetachedCriteria crit  = DetachedCriteria.forClass(MindTreeOcc.class)
		.add(Expression.eq("id", occ.getId()))		
		.setFetchMode("MindTree", FetchMode.JOIN)		
		.setFetchMode("MindTree.leftSide", FetchMode.JOIN)		
		.setFetchMode("MindTree.rightSide", FetchMode.JOIN);

		MindTreeOcc fullocc = (MindTreeOcc) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));		
		return fullocc.getMindTree();	
	}

	public WebLink getWebLinkForURI(String url, User user) {
		log.debug("user "+user.getUsername()+" url "+url);

		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(WebLink.class)
				.add(Expression.eq("user", user))
				.add(Expression.eq("uri", url)))
				.setFetchMode("topics", FetchMode.JOIN);

		List<WebLink> res = getHibernateTemplate().findByCriteria(crit);
		
		try {
			return (WebLink) DataAccessUtils.uniqueResult(res);
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn(e.getMessage()+" URL "+url);
			return res.get(0);
		}
		
	}

	public Topic load(long topicID) {
		return (Topic) getHibernateTemplate().load(Topic.class, topicID);
	}

	/**
	 * update the parameter.
	 * 
	 */
	public void populateUsageStats(UserPageBean rtn) {
		
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", rtn.getUser()))
		.add(Expression.ne("class", "association"))
		.add(Expression.ne("class", "seealso"))
		.add(Expression.ne("class", "metadate"))
		.setProjection(Projections.rowCount());
		
		rtn.setNumberOfTopics(DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(crit)));
		
		rtn.setNumberOfIslands(DataAccessUtils.intResult(getHibernateTemplate().find(""+
				"select count(id) from Tag tag "+
				"where user is ? "
				,rtn.getUser())));
		rtn.setNumberOfLinks(DataAccessUtils.intResult(getHibernateTemplate().find(""+
				"select count(id) from WebLink link "+
				"where user is ? "
				,rtn.getUser())));
		
	}

	public MindTree save(MindTree tree) {
		getHibernateTemplate().saveOrUpdate(tree);
		return tree;
	}

	public Occurrence save(Occurrence link) {
		getHibernateTemplate().saveOrUpdate(link);
		return link;
	}

	public Topic save(Topic t) throws HippoBusinessException {
		System.out.println("SAVE "+t.getTitle());
				
		//
		//Save the subject. If they've just added the subject it will be unsaved,
		//even if it's already in the DB, do a lookup.
		//
		Subject curSubj = t.getSubject();		
		if(curSubj != null && curSubj.getId() == 0){
			DetachedCriteria crit = DetachedCriteria.forClass(curSubj.getClass())
			.add(Expression.eq("foreignID", curSubj.getForeignID()));
			Subject saved = (Subject) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));
			if(saved == null){
				getHibernateTemplate().save(curSubj);
			}else{
				t.setSubject(saved);
			}
		}
	
		getHibernateTemplate().saveOrUpdate(t);		
	
		return t;		
	}

	public void saveSimple(Topic t){
		getHibernateTemplate().save(t);
	}

	public void saveTopicsLocation(long tagID, long topicID, double longitude, double latitude){
				
		log.debug("-------------SAVE TOPICS LOCATION---------------");
		log.debug("-------------tag "+tagID+" topic "+topicID+"---------------");	
		Topic t = (Topic) getHibernateTemplate().get(Topic.class, topicID);
		
		
		
		Set<TopicTypeConnector> types = t.getTypes();
		
		System.out.println(t.toPrettyString());
		
		log.debug("Types size "+t.getTypesAsTopics().size());
		log.debug("TypesWith loc size "+types.size());
		
		for(TopicTypeConnector twl : types){
		
			System.out.println("Found "+twl.getTopic().getTitle()+" lat "+twl.getLatitude()+" long "+twl.getLongitude());
			
			if(twl.getTopic().getId() == topicID){
				
				System.out.println("updating "+topicID);
				
				twl.setLatitude(latitude);
				twl.setLongitude(longitude);
				
				getHibernateTemplate().save(twl);
				break;
			}
		}
		
		
		
//		List<Object[]> list = getHibernateTemplate().find(""+
//				"select title, id, lastUpdated, top.types.latitude, top.types.longitude from Topic top "+
//				"where top.types.id is ? "+
//				"and user is ? "
//				,params);
//		
//		Topic t = (Topic) getHibernateTemplate().get(Topic.class, topicID);
//		t.getTypes().
		
	}

	public void tester() {
		// TODO Auto-generated method stub

	}

	
	public List<LocationDTO> getLocations(User user) {
		return getLocations(-1,user);
	}
	/**
	 * Pretty much the same code as getTimelines. Could refactor out common functionality.
	 * 
	 */
	public List<LocationDTO> getLocations(long tagID, User user) {
		List<LocationDTO> rtn = new ArrayList<LocationDTO>();

		//"where typeConn.type.class = MetaDate		
				
		//NOTE, aliases are INNER_JOINS so don't accidentally limt ourselves to only topics w/ tags
		DetachedCriteria crit = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.createAlias("associations", "assoc")				
		.createAlias("assoc.members", "metaValue")
		.createAlias("assoc.types", "assocTypeConn")
		.createAlias("assocTypeConn.type", "assocType")
		.add(Expression.eq("assocType.class", "metalocation"));
		
		if(tagID != -1){
			DetachedCriteria tags = DetachedCriteria.forClass(TopicTypeConnector.class)
			.add(Expression.eq("type.id", tagID))
			.setProjection(Property.forName("topic.id"));

			log.debug("Using tag subquery");
			crit.add(Subqueries.propertyIn("id", tags));
		}
	
		crit.setProjection(Projections.projectionList()
				.add(Property.forName("id"))
				.add(Property.forName("title"))
				.add(Property.forName("metaValue.id"))
				.add(Property.forName("metaValue.title"))
				.add(Property.forName("metaValue.latitude"))
				.add(Property.forName("metaValue.longitude"))
				.add(Property.forName("assocType.id"))
				.add(Property.forName("assocType.title")));
		
		List<Object[]>  ll = getHibernateTemplate().findByCriteria(crit);
		
		for (Object result : ll) {
			Object[] oa = (Object[]) result;
			for (int i = 0; i < oa.length; i++) {
				Object object = oa[i];
				if(object != null){
					System.out.println(" "+i+" "+object+" "+object.getClass());
				}else{
					System.out.println(" "+i+" "+object+" ");
				}
			}

			TopicIdentifier topic = new TopicIdentifier((Long)oa[0],(String)oa[1]);
			
			HippoLocation location = new HippoLocation();
			location.setId((Long) oa[2]);
			location.setTitle((String) oa[3]);
			location.setLatitude((Integer)oa[4]);
			location.setLongitude((Integer)oa[5]);
			
			MetaLocation meta = new MetaLocation();
			meta.setId((Long) oa[6]);
			meta.setTitle((String) oa[7]);
			
			LocationDTO locDTO = new LocationDTO(topic,location,meta);
			
			rtn.add(locDTO);						
		}
		
		return rtn;
	}


}
