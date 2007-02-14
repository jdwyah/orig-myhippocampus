package com.aavu.server.dao.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.web.domain.UserPageBean;

public class TopicDAOHibernateImpl extends HibernateDaoSupport implements TopicDAO{
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImpl.class);
	private static final int DEFAULT_AUTOCOMPLET_MAX = 7;

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
		.setFetchMode("associations", FetchMode.JOIN)
		.setFetchMode("associations.members", FetchMode.JOIN)	
		.setFetchMode("associations.types", FetchMode.JOIN);
	}

	/**
	 * remember, MetaDate objects only exist for one Tag ie a Book's ReadDate is not a 
	 * play's ReadDate.  We search on Tag, because we could be timeline-ing a Tag:
	 * RomanceBook extends Book, with Book's ReadDate
	 * 
	 */
	public List<TimeLineObj> getTimeline(User user) {
		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();

//		List<Object> ll= null;
//		(List<Object>) getHibernateTemplate().execute(new HibernateCallback(){
//		public Object doInHibernate(Session sess) throws HibernateException, SQLException {

//		return sess.createSQLQuery("select top.topic_id, top.title, metavalue.data from topic_meta_values "+
//		"join topics meta on topic_meta_values.topic_id = meta.topic_id "+
//		"join topics top on topic_meta_values.topic_meta_value_id = top.topic_id "+
//		"join topics metavalue on topic_meta_values.metaValue = metavalue.topic_id "+
//		"where meta.discriminator = 'metadate'")				
//		.list();

//		}});

		List<Object[]> ll = getHibernateTemplate().find("select top.id, top.title, metaValue.title from Topic top "+
				"join top.associations  ass "+				
				"join ass.types  typeConn "+
				"join ass.members metaValue "+
		"where typeConn.type.class = MetaDate and top.user = ?",user);


		for (Object topic : ll) {
			Object[] oa = (Object[]) topic;
			for (int i = 0; i < oa.length; i++) {
				Object object = oa[i];
				System.out.println(" "+i+" "+object+" "+object.getClass());
			}
			//?BigInteger topic_id = (BigInteger) oa[0];
			Long topic_id = (Long) oa[0];
			
			String dateStr = (String) oa[2];
			Date date = new Date(Long.parseLong(dateStr));			
			
			//add metaDate
			rtn.add(new TimeLineObj(new TopicIdentifier(topic_id.longValue(),(String)oa[1]),date,null));						
		}
	
		//add created
		//
//		List<Object[]> createdlist = getHibernateTemplate().find("select top.id, top.title, top.created from Topic top where user = ?",user);
//		for (Object topic : createdlist) {
//			Object[] oa = (Object[]) topic;
//			
//			//?BigInteger topic_id = (BigInteger) oa[0];
//			Long topic_id = (Long) oa[0];
//						
//			Date createdDateTimestamp = (Date) oa[2];			
//			//PEND GWT conversion in regular DAO code. should kinda move this to GWTService layer
//			Date createdDate = new Date(createdDateTimestamp.getTime());	
//			
//			//add date Created
//			rtn.add(new TimeLineObj(new TopicIdentifier(topic_id.longValue(),(String)oa[1]),createdDate,null));									
//		}
		
		if(log.isDebugEnabled()){
			log.debug(rtn.getClass());
			for (TimeLineObj obj : rtn) {
				log.debug("TIMELINE ");
				log.debug(obj);
			}
		}
		return rtn;	
	}

	public Topic getForName(User user, String string) {

		log.debug("user "+user.getUsername()+" string "+string);

		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
				.add(Expression.eq("user", user))
				.add(Expression.eq("title", string)));


		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));

		//return getHibernateTemplate().findByNamedParam("from Topic where user = :user and title = :title", "user", user);
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

	public void saveSimple(Topic t){
		getHibernateTemplate().save(t);
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

//		System.out.println("now set Associations : ");
//		for (Iterator iter = t.getAssociations().iterator(); iter.hasNext();) {			
//			Association assoc = (Association) iter.next();
//			System.out.println("assoc "+assoc+" size: "+assoc.getMembers().size());
//			System.out.println("assocDetail "+assoc.getTitle()+" "+assoc.getId());
//
//			for (Iterator iterator = assoc.getTypesAsTopics().iterator(); iterator.hasNext();) {
//				Topic type = (Topic) iterator.next();
//				assoc.setUser(t.getUser());
//				System.out.println("type "+type.getTitle());
//				//Why singleton?
//				//See description and Test Code in TopicDAOHibernateImplTest.testToMakeSureWeDontCreateTooManyObjects()
//				if(type instanceof MetaSeeAlso){
//					MetaSeeAlso singleton = (MetaSeeAlso) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from MetaSeeAlso"));
//					if(singleton == null){
//						System.out.println("single == null");
//						
//						//saveTwo(type, assoc);
//						getHibernateTemplate().saveOrUpdate(type);
//					}else{
//						System.out.println("single != null, rem/add");
//						System.out.println("assoc size "+assoc.getTypesAsTopics().size());
//						assoc.removeType(type);
//						System.out.println("assoc size "+assoc.getTypesAsTopics().size());
//						assoc.addType(singleton);
//						System.out.println("assoc size "+assoc.getTypesAsTopics().size());
//					}
//				}else{
//					
//					System.out.println("TYPE "+type.toPrettyString());
//					
//					
//					//saveTwo(type, assoc);
//					getHibernateTemplate().saveOrUpdate(type);
//					
//				}
//			}
//			
//			System.out.println("----------------------------");
//			System.out.println("about to save "+assoc.toPrettyString());
//			
//			getHibernateTemplate().saveOrUpdate(assoc);			
//		}



//		for (TopicTypeConnector conn : (Set<TopicTypeConnector>)t.getTypes()) {
//			System.out.println("TYPE "+conn.getTopic()+" "+conn.getType());
//			getHibernateTemplate().save(conn);
//		}
		
	
		getHibernateTemplate().saveOrUpdate(t);		
	
		return t;		
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

//		List<Object[]> list = getHibernateTemplate().find(""+
//		"select id from Association ass "+		
//		"where ? in elements(members) "+
//		"and user is ? "
//		,params);

		
		
		/*
		 * Get Topics of 'Type' this, ie "things on our island"
		 * 
		 * Note: Not sending this anymore, since a Topic already loads this.
		 */
//		List<Object[]> instancesOfThisTopicSlashTag = getHibernateTemplate().find(""+
//				"select title, id from Topic top "+
//				"where top.types.id is ? "+
//				"and user is ? "
//				,params);

		

		//associationsToThis.addAll(instancesOfThisTopicSlashTag);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(associationsToThis.size());
		for (Object[] o : associationsToThis){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}
		return rtn;		
	}

	public List<TopicTypeConnector> getTopicIdsWithTag(long tagid,User user) {
		
//		List<TopicTypeConnector> rtn = getHibernateTemplate().find("from Topic top left join top.types tagConn "+
//				"where tagConn.type.id = ?",new Long(tagid));
		
		List<TopicTypeConnector> rtn = getHibernateTemplate().find("from TopicTypeConnector conn "+
				"where conn.type.id = ? order by conn.topic.lastUpdated DESC",new Long(tagid));
				
		for (TopicTypeConnector connector : rtn) {
			System.out.println("DAO says "+connector.getId()+" "+connector.getLongitude()+" "+connector.getLatitude());
		}
		
		return rtn;
	}
	
	
	
	/*
	public List<FullTopicIdentifier> getTopicIdsWithTag(long tagid,User user) {			

		Object[] params = {new Long(tagid),user};		
		
//		List<Object[]> list = getHibernateTemplate().find(""+
//				"select title, id, lastUpdated from Topic top "+
//				"where top.types.id is ? "+
//				"and user is ? "
//				,params);
		
//		List<Object[]> list = getHibernateTemplate().find(""+
//				"select title, id, lastUpdated, top.types.latitude, top.types.longitude from Topic top "+
//				"where top.types.topic.id is ? "+
//				"and user is ? "
//				,params);
		
		List<Object[]> list = getHibernateTemplate().find(""+
				"select title, id, lastUpdated, top.types.latitude, top.types.longitude from Topic top "+
				"where ? in elements(top.types) "+
				"and user is ? "
				,params);
		
		
		List<Topic> topic = getHibernateTemplate().find("select inst.topic from Topic top "+
				"left join top.instances inst "+
				"where inst.type.id = 1041");
		
		
		
		
		//test query to prove uniqueness
//		List<Object[]> list = getHibernateTemplate().find(""+
//				"select title, id, lastUpdated, top.types.latitude, top.types.longitude from Topic top "+
//				"where top.types.id = 196 ");
		
		
		Set<FullTopicIdentifier> unique = new HashSet<FullTopicIdentifier>();
		
		//TODO we could probably be a good bit more efficient.
		//TODO Genericize: http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		log.debug("Found these topics on island: ");
		for (Object[] o : list){			
			FullTopicIdentifier ident = new FullTopicIdentifier((Long)o[1],(String)o[0],(Date) o[2],(Double)o[3],(Double)o[4]);
			log.debug(o[1]+" "+o[0]+"lat "+o[3]+" long "+o[4]);			
			unique.add(ident);			
		}

		List<FullTopicIdentifier> rtn = new ArrayList<FullTopicIdentifier>(unique.size());		
		rtn.addAll(unique);
		
		return rtn;		 		
	}*/

	/**
	 * Utility to set the projection properties for TopicIdentifier
	 * @return
	 */
	private Projection getTopicIdentifier(){
		return Projections.projectionList()
		.add(Property.forName("title"))
		.add(Property.forName("id"));
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
	

	/**
	 * TODO replace hardcoded class discriminators with .class
	 */	
	public List<TopicIdentifier> getAllTopicIdentifiers(User user) {

		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.add(Expression.ne("class", "association"))
		.add(Expression.ne("class", "seealso"))
		.add(Expression.ne("class", "metadate"))
		.add(Expression.ne("class", "date"))
		.add(Expression.ne("class", "text"))
		.addOrder( Order.asc("title").ignoreCase() )
		.setProjection(getTopicIdentifier());

//		List<Topic> l = getHibernateTemplate().findByCriteria(crit);
//		for (Topic topic : l) {
//		System.out.println("topic "+topic+"  class "+topic.getClass());
//		}

		List<Object[]> list = getHibernateTemplate().findByCriteria(crit);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}

		return rtn;
	}

	public Topic getForID(User user, long topicID) {
		
		
//		System.out.println("GET0");
//		List<TopicTypeConnector> ttc = getHibernateTemplate().find("from TopicTypeConnector");
//		for (TopicTypeConnector connector : ttc) {
//			System.out.println("conn "+connector.latitude+" "+connector.getLongitude()+" "+connector.getTopic().getId()+" "+connector.getType().getId());
//		}
//		List<Topic> topic = getHibernateTemplate().find("from Topic top where top.instances.size > 0");
//		for (Topic topic2 : topic) {
//			System.out.println("found "+topic2+" "+topic2.getId());
//		}
		
		
//		System.out.println("THIS WORKS");
//		List<Topic> topic = getHibernateTemplate().find("select inst.topic from Topic top "+
//		"left join top.instances inst "+
//		"where inst.type.id = 1041");
//		
//		
//		for (Topic topic2 : topic) {
//			System.out.println("found "+topic2+" "+topic2.getId());	
//		}
		

		
		
		
//		System.out.println("FLUSH");
//		getHibernateTemplate().flush();
//		System.out.println("GETTING");
//		Topic rtn = (Topic) getHibernateTemplate().get(Topic.class, topicID);		
//		System.out.println("GOT");
//		System.out.println(rtn.toPrettyString());
//				
//		return rtn;
		
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
				.add(Expression.eq("user", user))
				.add(Expression.eq("id", topicID)));

		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));			
	}

	public void tester() {
		// TODO Auto-generated method stub

	}

	/**
	 * TESTING ONLY
	 * Should not be exposed to Service layer.
	 * 
	 * for when you really want the whole DB
	 * 
	 */
	public List<Topic> getAllTopics() {		
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class));
		return getHibernateTemplate().findByCriteria(crit);
	}
	/**
	 * TESTING ONLY
	 * Should not be exposed to Service layer.
	 * 
	 * for when you really want the whole DB
	 * 
	 */
	public void deleteAllTables() {
		throw new UnsupportedOperationException();
//		getHibernateTemplate().execute(new HibernateCallback(){
//
//			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
//
//				Connection conn = sess.connection();
//				Statement statement = conn.createStatement();
//
//				boolean res = statement.execute("DELETE FROM topic_scopes");
//				res = statement.execute("DELETE FROM topic_associations");
//				res = statement.execute("DELETE FROM instancetable");
//				res = statement.execute("DELETE FROM member_topics");
//				res = statement.execute("DELETE FROM topic_occurences");
//				res = statement.execute("DELETE FROM occurrences");
//				res = statement.execute("DELETE FROM subjects");
//				res = statement.execute("DELETE FROM typetable");
//				res = statement.execute("DELETE FROM topics");				
//				return res;
//
//			}});

	}

	public Occurrence save(Occurrence link) {
		getHibernateTemplate().saveOrUpdate(link);
		return link;
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
	 * Note don't sysout fullocc since it's User is not loaded and that LazyLEx's
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
	public MindTree save(MindTree tree) {
		getHibernateTemplate().saveOrUpdate(tree);
		return tree;
	}

	/**
	 * update the parameter
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

	/**
	 * 
	 */
	public void delete(final Topic todelete) {
		
	log.info("Deleting: "+todelete);
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
					//connector.getType()
					System.out.println("Delete connection "+connector);
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

	public Topic load(long topicID) {
		return (Topic) getHibernateTemplate().load(Topic.class, topicID);
	}

	public Topic get(long topicID) {
		return (Topic) getHibernateTemplate().get(Topic.class, topicID);
	}

	public MetaSeeAlso getSeeAlsoSingleton() {
		return (MetaSeeAlso) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from MetaSeeAlso"));
	}


}
