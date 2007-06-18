package com.aavu.server.dao.hibernate;


public class Deadwood {

//	public List<TimeLineObj> getTimeline(long metaId,User user) {
//		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();

//	List<Object> ll= null;
//	(List<Object>) getHibernateTemplate().execute(new HibernateCallback(){
//	public Object doInHibernate(Session sess) throws HibernateException, SQLException {

//	return sess.createSQLQuery("select top.topic_id, top.title, metavalue.data from topic_meta_values "+
//	"join topics meta on topic_meta_values.topic_id = meta.topic_id "+
//	"join topics top on topic_meta_values.topic_meta_value_id = top.topic_id "+
//	"join topics metavalue on topic_meta_values.metaValue = metavalue.topic_id "+
//	"where meta.discriminator = 'metadate'")				
//	.list();

//	}});


	
//	List<Object[]> ll2 = getHibernateTemplate().find("select top.id, top.title, metaValue.title from Topic top "+
//			"join top.associations  ass "+				
//			"join ass.types  typeConn "+
//			"join ass.members metaValue "+
//	"where typeConn.type.class = MetaDate and top.user = ?",user);
	
	
//	add created
	//
//	List<Object[]> createdlist = getHibernateTemplate().find("select top.id, top.title, top.created from Topic top where user = ?",user);
//	for (Object topic : createdlist) {
//		Object[] oa = (Object[]) topic;
//		
//		//?BigInteger topic_id = (BigInteger) oa[0];
//		Long topic_id = (Long) oa[0];
//					
//		Date createdDateTimestamp = (Date) oa[2];			
//		//PEND GWT conversion in regular DAO code. should kinda move this to GWTService layer
//		Date createdDate = new Date(createdDateTimestamp.getTime());	
//		
//		//add date Created
//		rtn.add(new TimeLineObj(new TopicIdentifier(topic_id.longValue(),(String)oa[1]),createdDate,null));									
//	}
	
//}
	
	
	
//	public List<TopicIdentifier> getLinksTo(Topic topic,User user) {
//		Object[] params = {topic.getId(),user};
//		log.debug("----------getLinksTo-----------");
//		log.debug("------------"+topic+"-------");
//		
//		/*
//		 * Get Associations that mention this Topic
//		 */
//		List<Object[]> associationsToThis = getHibernateTemplate().find(""+
//				"select title, id from Topic top "+		
//				//"join top.associations "+
//				"where ? in elements(top.associations.members) "+
//				"and user is ? "
//				,params);
//
////		List<Object[]> list = getHibernateTemplate().find(""+
////		"select id from Association ass "+		
////		"where ? in elements(members) "+
////		"and user is ? "
////		,params);
//
//		
//		
//		/*
//		 * Get Topics of 'Type' this, ie "things on our island"
//		 * 
//		 * Note: Not sending this anymore, since a Topic already loads this.
//		 */
////		List<Object[]> instancesOfThisTopicSlashTag = getHibernateTemplate().find(""+
////				"select title, id from Topic top "+
////				"where top.types.id is ? "+
////				"and user is ? "
////				,params);
//
//		
//
//		//associationsToThis.addAll(instancesOfThisTopicSlashTag);
//
//		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(associationsToThis.size());
//		for (Object[] o : associationsToThis){
//			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
//		}
//		return rtn;		
//	}
	

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
	
	
	
//public Topic getForID(User user, long topicID) {
		
		
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
		
//		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
//				.add(Expression.eq("user", user))
//				.add(Expression.eq("id", topicID)));
//
//		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));			
//	}




///**
// * TESTING ONLY
// * Should not be exposed to Service layer.
// * 
// * for when you really want the whole DB
// * 
// */
//public void deleteAllTables() {
//	throw new UnsupportedOperationException();
////	getHibernateTemplate().execute(new HibernateCallback(){
////
////		public Object doInHibernate(Session sess) throws HibernateException, SQLException {
////
////			Connection conn = sess.connection();
////			Statement statement = conn.createStatement();
////
////			boolean res = statement.execute("DELETE FROM topic_scopes");
////			res = statement.execute("DELETE FROM topic_associations");
////			res = statement.execute("DELETE FROM instancetable");
////			res = statement.execute("DELETE FROM member_topics");
////			res = statement.execute("DELETE FROM topic_occurences");
////			res = statement.execute("DELETE FROM occurrences");
////			res = statement.execute("DELETE FROM subjects");
////			res = statement.execute("DELETE FROM typetable");
////			res = statement.execute("DELETE FROM topics");				
////			return res;
////
////		}});
//
//}
	
	
	
	

//	@SuppressWarnings("unchecked")
//	public List<TimeLineObj> getTimeline(Set<MetaDate> limitToTheseMetas, Set<Tag> limitToTheseTags,User user) {
//		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();
//
//		
//		List<Object[]> ll;
//		
//		//"where typeConn.type.class = MetaDate		
//		
//		//NOTE, aliases are INNER_JOINS so don't accidentally limt ourselves to only topics w/ tags
//		DetachedCriteria crit = DetachedCriteria.forClass(Topic.class)
//		.add(Expression.eq("user", user))
//		.createAlias("associations", "assoc")				
//		.createAlias("assoc.members", "metaValue")
//		.createAlias("assoc.types", "assocTypeConn")
//		.createAlias("assocTypeConn.type", "assocType");
//		
//		
//		if(limitToTheseMetas.isEmpty()){	
//			System.out.println("ALL META");
//			crit.add(Expression.eq("assocType.class", "metadate"));
//		}else{
//			System.out.println("Only THIS META");
//			MetaDate meta = (MetaDate) limitToTheseMetas.iterator().next();
//			crit.add(Expression.eq("assocType.id", meta.getId()));			
//		}		
//		
//		//if(!limitToTheseTags.isEmpty()){
////			crit.setFetchMode("types", FetchMode.JOIN);
////			crit.setFetchMode("associations", FetchMode.JOIN)
////			.setFetchMode("types.type", FetchMode.JOIN);
//		//}
//			
//			
////		if(!limitToTheseTags.isEmpty()){
////			System.out.println("NOT EMPTY ONLY TAG");
////						
////			crit.createAlias("types", "typeConn")		
////			.createAlias("typeConn.type", "topicType");
////			
////			
////			Set<Long> limits = new HashSet<Long>();
////			limits.add(new Long(3333));
////			
////			List<TopicTypeConnector> rtn = getHibernateTemplate().find("from TopicTypeConnector conn "+
////					"where conn.type.id = ? order by conn.topic.lastUpdated DESC",new Long(tagid));
////			Subqueries.
////			
////			DetachedCriteria topicsIsOfTheseTags = DetachedCriteria.forClass(Topic.class)
////			.add(Expression.in("id", limits))
////			.setProjection(Property.forName("id"));
////			
////			crit.add(Subqueries.in("id", topicsIsOfTheseTags));
////			
////		}
//		if(!limitToTheseTags.isEmpty()){
//			System.out.println("\n\n---------------------------------------------");
//			crit.setFetchMode("types", FetchMode.JOIN);
//			List<Topic> topicsL = getHibernateTemplate().findByCriteria(crit);
//			
//			System.out.println("number of topics "+topicsL.size());
//			
//			for (Topic topic : topicsL) {
//				System.out.println("topic "+topic);
//			}
//			
//			Set<Topic> topics = new HashSet<Topic>();
//			topics.addAll(topicsL);
//		
//			
//			System.out.println("number of topics after unique "+topics.size());
//			for (Topic topic : topics) {
//				System.out.println("topic "+topic);
//			}
//			for (Topic topic : topics) {
//				
//				System.out.println("topic "+topic);
//				
//				boolean found = false;
//				for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
//					Tag tag = (Tag) iter.next();
//					
//					if(limitToTheseTags.contains(tag)){
//						found = true;
//					}
//					System.out.println("Check "+tag+" Found: "+found);
//				}
//				if(!found){
//					continue;
//				}
//				
//				for (Iterator iter = topic.getMetas().iterator(); iter.hasNext();) {
//					
//					Meta meta = (Meta) iter.next();
//					System.out.println("META "+meta);
//					for (Iterator iterator = topic.getMetaValuesFor(meta).iterator(); iterator
//							.hasNext();) {						
//						HippoDate date = (HippoDate) iterator.next();
//						System.out.println("VALUE "+date);
//						rtn.add(new TimeLineObj(topic.getIdentifier(),date.getDate(),null));
//					}
//				}
//				
//				
////				String dateStr = (String) oa[2];
////				Date date = new Date(Long.parseLong(dateStr));			
////
////				//add metaDate
////				rtn.add(new TimeLineObj(new TopicIdentifier(topicId.longValue(),(String)oa[1]),date,null));
//			}
//		}else{
//			crit.setProjection(Projections.projectionList()
//					.add(Property.forName("id"))
//					.add(Property.forName("title"))
//					.add(Property.forName("metaValue.title"))
//					.add(Property.forName("types"))
//					.add(Property.forName("associations")));
//
//
//			ll = getHibernateTemplate().findByCriteria(crit);
//
////			//metaId == TimeLineObj.ALL_METAS 
////			if(limitToTheseMetas.isEmpty()){
////			Object[] params = new Object[2];			
////			params[1] = user;
////			ll = getHibernateTemplate().find("select top.id, top.title, metaValue.title from Topic top "+
////			"join top.associations  ass "+				
////			"join ass.types  typeConn "+
////			"join ass.members metaValue "+
////			"where ass.members.class = HippoDate and top.user = ?",params);
////			}else{
////			Object[] params = new Object[2];
//
////			params[0] = (Long) limitToTheseMetas.iterator().next();
////			params[1] = user;
////			ll = getHibernateTemplate().find("select top.id, top.title, metaValue.title from Topic top "+
////			"join top.associations  ass "+				
////			"join ass.types  typeConn "+
////			"join ass.members metaValue "+
////			"where typeConn.type.id = ? and top.user = ?",params);
////			}
//
//			for (Object topic : ll) {
//				Object[] oa = (Object[]) topic;
//				for (int i = 0; i < oa.length; i++) {
//					Object object = oa[i];
//					if(object != null){
//						System.out.println(" "+i+" "+object+" "+object.getClass());
//					}else{
//						System.out.println(" "+i+" "+object+" ");
//					}
//				}
//				//?BigInteger topic_id = (BigInteger) oa[0];
//				Long topicId = (Long) oa[0];
//
//				String dateStr = (String) oa[2];
//				Date date = new Date(Long.parseLong(dateStr));			
//
//				//add metaDate
//				rtn.add(new TimeLineObj(new TopicIdentifier(topicId.longValue(),(String)oa[1]),date,null));						
//			}
//		}
//		
//		
//		if(log.isDebugEnabled()){
//			log.debug(rtn.getClass());
//			for (TimeLineObj obj : rtn) {
//				log.debug("TIMELINE ");
//				log.debug(obj);
//			}
//		}
//		return rtn;	
//	}

//	public Tag upgradeToTag(final Topic t) {
//
//		int res = (Integer) getHibernateTemplate().execute(new HibernateCallback(){
//			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
//				String hqlUpdate = "update Topic set discriminator = 'tag' where topic_id = :id";
//				int updatedEntities = sess.createQuery( hqlUpdate )
//				.setLong( "id", t.getId() )	                            
//				.executeUpdate();				
//				return updatedEntities;				
//			}});
//		log.debug("res: "+res);
//		
//		getHibernateTemplate().evict(t);
//		
//		DetachedCriteria crit  =  SelectDAOHibernateImpl.loadEmAll(DetachedCriteria.forClass(Tag.class)
//				.add(Expression.eq("id", t.getId())));
//		
//		return (Tag) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));		
//		
//	}
}
