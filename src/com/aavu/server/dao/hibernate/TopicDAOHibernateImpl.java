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
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import sun.security.jca.GetInstance;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.web.domain.UserPageBean;

public class TopicDAOHibernateImpl extends HibernateDaoSupport implements TopicDAO{
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImpl.class);

	public static DetachedCriteria loadEmAll(DetachedCriteria crit){
		return crit.setFetchMode("user", FetchMode.JOIN)		
		.setFetchMode("instances", FetchMode.JOIN)
		.setFetchMode("subject", FetchMode.JOIN)
		.setFetchMode("types", FetchMode.JOIN)
		.setFetchMode("types.associations", FetchMode.JOIN)
		.setFetchMode("types.associations.types", FetchMode.JOIN)
		.setFetchMode("types.associations.members", FetchMode.JOIN)
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
				"join ass.types  type "+
				"join ass.members metaValue "+
		"where type.class = MetaDate and top.user = ?",user);


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

		return getHibernateTemplate().findByCriteria(crit);
	}



	public Topic save(Topic t) throws HippoBusinessException {
		System.out.println("SAVE "+t.getTitle());

		if(t.getTitle().equals("")){
			log.info("Throw HBE exception for Empty Title");
			throw new HippoBusinessException("Empty Title");
		}	
		if(t.mustHaveUniqueName()){
			log.debug("Getting same named");
			Object[] args = {t.getTitle(),t.getUser()};
			Topic sameNamed = (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from Topic where title = ? and user = ?",args));
			log.debug("Rec "+sameNamed);		

			if(sameNamed != null && sameNamed.getId() != t.getId()){
				log.info("Throw HBE exception for Duplicate Title");
				throw new HippoBusinessException("Duplicate Name");
			}
			//need to evict or we'll get a NonUniqueException
			getHibernateTemplate().evict(sameNamed);
		}
		
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

//		//NOTE: Saving the user here. 
//		//Where should the user really be getting saved? We're saving it to 
//		//Topic in Service layer. 
//		for (Iterator iter = t.getOccurences().iterator(); iter.hasNext();) {			
//		Occurrence occur = (Occurrence) iter.next();
//		if(occur.getUser() == null){
//		occur.setUser(t.getUser());
//		}
//		getHibernateTemplate().save(occur);
//		}

		//
		//Bit of a chicken & egg thing here with TransientReferences..
		//



//		System.out.println("METAS "+t.getMetas().size());
//		for (Iterator iter = t.getMetas().iterator(); iter.hasNext();) {
//		Meta meta = (Meta) iter.next();
//		log.debug("saving its metas ? "+meta.getTitle());		
//		if(meta.getId() == 0){			
//		log.debug("was unsaved. save"+meta.getTitle());
//		getHibernateTemplate().saveOrUpdate(meta);
//		}
//		log.debug("done");
//		}

//		System.out.println("METAValues "+t.getMetaValues().entrySet().size());
//		for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
//		Topic metaValue = (Topic) t.getMetaValues().get(iter.next());
//		log.debug("saving its metavalue element ? "+metaValue.getTitle());		
//		if(metaValue.getId() == 0){			
//		log.debug("was unsaved. save"+metaValue.getTitle());
//		getHibernateTemplate().saveOrUpdate(metaValue);
//		}
//		log.debug("done");
//		}

		System.out.println("now set Associations : ");
		for (Iterator iter = t.getAssociations().iterator(); iter.hasNext();) {			
			Association assoc = (Association) iter.next();
			System.out.println("assoc "+assoc+" size: "+assoc.getMembers().size());
			System.out.println("assocDetail "+assoc.getTitle()+" "+assoc.getId());

			for (Iterator iterator = assoc.getTypes().iterator(); iterator.hasNext();) {
				Topic type = (Topic) iterator.next();
				assoc.setUser(t.getUser());
				//Why singleton?
				//See description and Test Code in TopicDAOHibernateImplTest.testToMakeSureWeDontCreateTooManyObjects()
				if(type instanceof MetaSeeAlso){
					MetaSeeAlso singleton = (MetaSeeAlso) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from MetaSeeAlso"));
					if(singleton == null){
						getHibernateTemplate().saveOrUpdate(type);
					}else{
						assoc.getTypes().remove(type);
						assoc.getTypes().add(singleton);
					}
				}else{
					getHibernateTemplate().saveOrUpdate(type);
				}
			}
			getHibernateTemplate().saveOrUpdate(assoc);			
		}


		System.out.println("and middle Save me "+t.getTitle()+" type: "+t.getClass());
		//and save me
		//
		
		getHibernateTemplate().saveOrUpdate(t);		

		//call saveList() instead

//		System.out.println("now TYPES "+t.getTypes().size());
//		for (Iterator iter = t.getTypes().iterator(); iter.hasNext();) {
//		Topic type = (Topic) iter.next();
//		log.debug("saving it's ? "+type.getTitle());

//		//this let's us save a topic and make it's tag save too. 
//		//needs to happen AFTER the save of the topic, or it will 
//		//have a reference to the non-persisted topic.
//		//
//		//NOTE: having this after means, that all of a topic's tags
//		//must be saved before this method is called or we'll get the 
//		//reverse problem. This is different than metas which can be unsaved
//		//and then will be saved above if new.
//		//
//		log.debug("save "+type.getTitle()+" "+type.getId());

//		Topic prev = (Topic) getHibernateTemplate().get(Topic.class, type.getId());
//		if(prev != null){
//		//log.debug("prev "+prev+" instance size "+prev.getInstances().size());
//		prev.getInstances().addAll(type.getInstances());
//		}else{
//		log.debug("no prev ");
//		getHibernateTemplate().saveOrUpdate(type);
//		}
//		log.debug("done");
//		}



//		if (t instanceof Association) {			
//		Association assoc = (Association) t;
//		System.out.println("ASSOC ");
//		//
//		//key's aren't topic anymore. just "TO" "FROM" etc
//		//
//		for (Iterator iter = assoc.getMembers().values().iterator(); iter.hasNext();) {
//		Topic e = (Topic) iter.next();
//		System.out.println("e "+e);
//		System.out.println("id "+e.getId());
//		Topic realEntry = (Topic) getHibernateTemplate().get(Topic.class, e.getId());
//		System.out.println("real "+realEntry);
//		realEntry.getAssociations().add(assoc);				
//		getHibernateTemplate().saveOrUpdate(realEntry);				
//		}

//		getHibernateTemplate().saveOrUpdate(assoc);
//		}
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

	public List<TopicIdentifier> getTopicIdsWithTag(Tag tag,User user) {			

		Object[] params = {tag.getId(),user};				
		List<Object[]> list = getHibernateTemplate().find(""+
				"select title, id from Topic top "+
				"where top.types.id is ? "+
				"and user is ? "
				,params);


		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO Genericize: http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}

		return rtn;		 		
	}

	/**
	 * Utility to set the projection properties for TopicIdentifier
	 * @return
	 */
	private Projection getTopicIdentifier(){
		return Projections.projectionList()
		.add(Property.forName("title"))
		.add(Property.forName("id"));
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

		getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session sess) throws HibernateException, SQLException {

				Connection conn = sess.connection();
				Statement statement = conn.createStatement();

				boolean res = statement.execute("DELETE FROM topic_scopes");
				res = statement.execute("DELETE FROM topic_associations");
				res = statement.execute("DELETE FROM instancetable");
				res = statement.execute("DELETE FROM member_topics");
				res = statement.execute("DELETE FROM topic_occurences");
				res = statement.execute("DELETE FROM occurrences");
				res = statement.execute("DELETE FROM subjects");
				res = statement.execute("DELETE FROM typetable");
				res = statement.execute("DELETE FROM topics");				
				return res;

			}});

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
				
				log.info("Instances: "+topic.getInstances().size());
				for (Topic instance : (Set<Topic>)topic.getInstances()) {
					log.info("Was a tag. Removing instances "+instance);
					instance.getTypes().remove(topic);
				}
				topic.getInstances().clear();
				
				log.info("Types: "+topic.getTypes().size());
				for (Topic type : (Set<Topic>)topic.getTypes()) {
					log.info("Removing from type "+type);
					type.getInstances().remove(topic);
				}
				topic.getTypes().clear();
				
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
				
				for (Topic assoc : (Set<Topic>)topic.getAssociations()) {
					//TODO what here? any cascade?					
				}
				
				sess.delete(topic);				
				return true;
			}});
	}



}
