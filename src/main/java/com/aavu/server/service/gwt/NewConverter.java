package com.aavu.server.service.gwt;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.hibernate.collection.PersistentSet;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.generated.AbstractTopic;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.CGLibNeedsCloning;

public class NewConverter {
	private static final Logger log = Logger.getLogger(NewConverter.class);


	/**
	 * fixes hibernate.collection.persistentset, java.sql.date, cglib enhanced stuff.
	 * 
	 * Reflection based. Old solution (commented at bottom) was a specific conversion based on known
	 * fields. Moved to this solution to try to be less fragile, but this isn't exactly graveful
	 * either.
	 * 
	 * NOTE: Consider applying this with AOP, that would get rid of the entire GWTService layer.
	 * 
	 * NOTE: this may null Topic->TypeConnectors->Type->TypeConnectors->Topic (ie if yirgacheffe is
	 * tagged with coffee, coffee's link to yirgacheffe may be nulled) This was done as part of the
	 * code to fix infinite recursion problems using the map convertList.
	 * 
	 * @param object
	 */
	public static void convertInPlace(Object object) {
		Class objectClass = object.getClass();
		Map<Object, Object> convertList = new HashMap();
		convertList.put(object, null);
		try {
			convertInPlace(object, objectClass, convertList, objectClass.getName());
		} catch (CGLibNeedsCloning e) {
			log.error("CGLib Null Error shouldn't trickle up.");
		}
	}

	/**
	 * This is the method that should be called for recursion, since it will protect us from
	 * infinite topic recusion.
	 * 
	 * @param object
	 * @param haveConverted
	 * @throws CGLibNeedsCloning
	 */
	public static void convertInPlace(Object object, Map onConvertList, String dbg)
			throws CGLibNeedsCloning {
		Class objectClass = object.getClass();
		convertInPlace(object, objectClass, onConvertList, dbg);
	}

	public static void convertInPlace(Object object, Class objectClass, Map onConvertList,
			String dbg) throws CGLibNeedsCloning {

		log.info("Doing " + object + " " + objectClass.getName() + " ");

		// CGLIB
		// 
		// ie com.aavu.client.domain.Topic$$EnhancerByCGLIB$$bdac6e52
		if (objectClass.getName().contains("CGLIB")) {
			log.info(dbg + " Found CGLIB enhanced object. Null-ing");
			throw new CGLibNeedsCloning();
		}

		// we've got properties to convert
		if (objectClass.getName().contains("Abstract")) {
			log.debug("Was abstract: " + objectClass.getName());
			convertProperties(object, objectClass, onConvertList, dbg);
		}


		// end of the road
		if (objectClass.getName().contains("Object")) {
			log.debug("Hit Object, done");
			return;
		}
		// go to superclass
		else {
			log.debug("Do Super");
			log.debug("S " + objectClass.getSuperclass());

			convertInPlace(object, objectClass.getSuperclass(), onConvertList, dbg);
		}

	}

	private static Object convert(Object o, Set onConvertList, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Convert the fields defined by this objectClass if they are:
	 * 
	 * 1) Date. We need to convert all sql.Date to util.Date 2) All sets need to go from
	 * hibernate.Collections.PersistantSet to HashSet 3) TopicTypeConnectors need to be recursively
	 * done
	 * 
	 * 
	 * @param object
	 * @param objectClass
	 * @param onConvertList
	 */
	private static void convertProperties(Object object, Class objectClass, Map onConvertList,
			String dbg) {

		log.debug("obj type " + objectClass.getName());
		log.debug("NUM FIELDS " + objectClass.getDeclaredFields().length);
		log.debug("NUM METHODS " + objectClass.getDeclaredMethods().length);



		Method[] methods = objectClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			try {
				if (!methods[i].getName().startsWith("get"))
					continue;

				if (dbg.endsWith("!!")) {
					log.debug("\n\n\n");
				}
				log.debug("DO METHOD: " + methods[i].getName());

				// get return type of a method
				Class methodReturnType = methods[i].getReturnType();

				String methodName = methods[i].getName();
				methodName = "set" + methodName.substring(3);

				Object[] res = new Object[1];

				log.debug("method rtn " + methodReturnType.getName());

				if (isDate(methodReturnType)) {

					log.debug("running date conversion");

					res[0] = methods[i].invoke(object, null);

					if (res[0] != null) {
						Method setter = objectClass.getMethod(methodName, methodReturnType);
						Date convertedDate = new Date(((Date) res[0]).getTime());
						setter.invoke(object, convertedDate);
					}

				} else if (needsRecurse(methodReturnType)) {
					log.info("Running nested Topic conversion: " + dbg + " "
							+ methodReturnType.getName());
					res[0] = methods[i].invoke(object, null);

					log.debug("isTopic res[0] " + res[0]);


					Method setter = objectClass.getMethod(methodName, methodReturnType);
					if (res[0] != null) {
						if (!onConvertList.containsKey(res[0])) {

							// add b4 conversion, otherwise it won't do any good
							log.warn("FINISHING: " + res[0] + " " + dbg + "->" + methodName);
							onConvertList.put(res[0], null);

							Object toSet = null;
							log.info("T_CONV_1->" + res[0].getClass());
							try {
								convertInPlace(res[0], onConvertList, dbg + "->"
										+ methods[i].getName());
								toSet = (Object) res[0];

							} catch (CGLibNeedsCloning e) {
								// TODO what if it's a CGLib Subject!
								log.info("caught cglib, shallow cloning");

								if (res[0] instanceof Topic) {
									// Shallow Clone
									Topic t = (Topic) res[0];
									toSet = new RealTopic(t.getUser(), t.getTitle());
									((Topic) toSet).setId(t.getId());
								} else if (res[0] instanceof Subject) {
									Subject s = (Subject) res[0];
									toSet = s.clone();
								}
							}
							if (toSet != null) {
								log.info("T_CONV_2->" + toSet.getClass());
							}
							setter.invoke(object, toSet);
							onConvertList.put(res[0], toSet);

						} else {
							log.info("foooooooooooo");

							Object fromList = onConvertList.get(res[0]);
							log.warn("FINISHING: " + res[0] + " " + dbg + "->" + methodName
									+ " with " + fromList);
							// log.info(onConvertList.get(res[0]).getClass()+"");

							if (fromList != null) {
								log.info("No recurse. Using " + onConvertList.get(res[0]) + " "
										+ onConvertList.get(res[0]).getClass());
							} else {
								log.info("No recurse. Using NULL");
							}
							setter.invoke(object, fromList);
						}
					}
				} else if (isSet(methodReturnType)) {

					log.info("Runing collection " + dbg + " " + methodReturnType.getName());

					try {

						res[0] = methods[i].invoke(object, null);


						log.debug("checking ");

						if (res[0] instanceof PersistentSet) {

							log.debug("Fixing Persistent ");

							PersistentSet persSet = (PersistentSet) res[0];

							if (!persSet.wasInitialized()) {
								log.debug("un-initialized setting to new HashSet()");
								res[0] = new HashSet();
							}
						}

						Set set = (Set) res[0];
						log.debug("size: " + set.size());
						HashSet rtn = new HashSet();
						log.debug("||||||||||||");
						for (Iterator iter = ((Collection) res[0]).iterator(); iter.hasNext();) {
							Object o = iter.next();
							log.info("--------------recursing into set " + o.getClass());

							if (o.getClass() == TopicTypeConnector.class) {
								TopicTypeConnector c = (TopicTypeConnector) o;
								log
										.debug("-TOPIC: " + c.getTopic().getClass() + " "
												+ c.getTopic());
								log.debug("-TYPE: " + c.getType().getClass() + " " + c.getType());
							}

							if (o.getClass() == TopicTypeConnector.class) {
								convertInPlace(o, onConvertList, dbg + "->" + methods[i].getName()
										+ "!!");
							} else {
								convertInPlace(o, onConvertList, dbg + "->" + methods[i].getName());
							}
							log.info("=============Just recursed. " + o + " " + o.getClass());
							if (o.getClass() == TopicTypeConnector.class) {
								TopicTypeConnector c = (TopicTypeConnector) o;
								if (c.getTopic() != null) {
									log.debug("=TOPIC: " + c.getTopic().getClass() + " "
											+ c.getTopic());
								}
								if (c.getType() != null) {
									log.debug("=TYPE: " + c.getType().getClass() + " "
											+ c.getType());
								}
							}
							rtn.add(o);

						}
						log.debug("EEEEEEEEEEEEE");
						res[0] = rtn;


					} catch (LazyInitializationException lie) {
						log.warn("Caught Lazy. Unexpected.");
						res[0] = new HashSet();
					}



					log.info("invoke " + methodName + " setting with class " + res[0].getClass());
					Method setter = objectClass.getMethod(methodName, methodReturnType);
					setter.invoke(object, res[0]);
				}

			} catch (Exception e) {
				log.error("Exception " + e);
				e.printStackTrace();
			}
		}
	}


	/**
	 * Could replace this with a more generic "is not primitive"
	 * 
	 * @param methodReturnType
	 * @return
	 */
	private static boolean needsRecurse(Class methodReturnType) {
		// System.out.println("check "+methodReturnType);
		return methodReturnType == Topic.class || methodReturnType == Subject.class;
	}

	private static boolean isSet(Class methodReturnType) {
		return methodReturnType == Set.class;
	}

	private static boolean isDate(Class methodReturnType) {
		return methodReturnType == java.util.Date.class;
	}


	// public static Topic convert(Topic t){
	//
	// return convert(t,0);
	// }
	// public static Topic convert(Topic t,int level){
	// return convert(t,level,false,false);
	// }
	// public static Topic convert(Topic t,int level,boolean hasMembers,boolean
	// typesWithAssociations){
	// log.debug("CONVERT Topic "+t+" level: "+level+" members "+hasMembers);
	// log.debug("Topic : "+t.getId()+" "+t.getTitle()+" tags:"+t.getTypesAsTopics().getClass());
	//
	//
	//
	// log.debug("t "+t.getTypesAsTopics().getClass());
	//			
	// t.setInstances(new HashSet());
	//			
	// //
	// //new-ing it is essentially nulling it out, since we can't pass
	// //lazy stuff
	// //
	// //L2 new it out
	// //L1 new everything expect metas we need this for topic's->tag's->metas
	// //
	// if(level >= 2){
	// if(null == t){
	// return t;
	// }
	// System.out.println("setting nul_____________________________________________");
	// t.setLastUpdated(null);
	// t.setCreated(null);
	// t.setSubject(null);
	//
	// t.setScopes(new HashSet());
	// //t.setInstances(new HashSet());
	// t.setOccurences(new HashSet());
	// t.setAssociations(new HashSet());
	//
	// if(hasMembers){
	// log.debug("LEVEL 2 HAS MEMBERS");
	//
	// Association ass = (Association) t;
	//
	// log.debug("types ");
	// log.debug("size "+ass.getTypes().size());
	// ass.setTypes(converter(ass.getTypes(), level));
	//
	// log.debug("members ");
	// log.debug("size "+ass.getMembers().size());
	// ass.setMembers(converter(ass.getMembers(), level));
	// }else{
	// //is this necessary?
	// //
	// t.setTypes(new HashSet());
	// }
	//
	// }else if(level == 1){
	// if(null == t){
	// return t;
	// }
	// log.debug("l1 "+t);
	// log.debug("last: "+t.getLastUpdated());
	// log.debug("created: "+t.getCreated());
	//
	// //didn't need to convert the postgres one, but mysql is
	// //returning java.sql.timestamp, which, surprise surprise
	// //is another thing that breaks GWT serialization.
	// //
	// //hmm, I think these get nulled in Level 2 set -> client,
	// //then we error when the client passes this back. blech.
	// //
	// if(t.getLastUpdated() != null && t.getCreated() != null){
	// t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
	// t.setCreated(new Date(t.getCreated().getTime()));
	// }
	//
	//
	// t.setScopes(new HashSet());
	// //t.setInstances(new HashSet());
	// t.setOccurences(new HashSet());
	// t.setSubject(null);
	//
	// log.debug("has Members "+hasMembers);
	//
	// if(typesWithAssociations){
	// log.debug("LEVEL 1 typesWithAssociations");
	// t.setAssociations(converter(t.getAssociations(), level,true));
	// }else{
	// t.setAssociations(new HashSet());
	// }
	//
	// //convert associations
	// if(hasMembers){
	// Association ass = (Association) t;
	//
	// log.debug("types ");
	// t.setTypes(converter(t.getTypes(), level));
	//
	// log.debug("members ");
	// ass.setMembers(converter(ass.getMembers(), level));
	// }else{
	// t.setTypes(new HashSet());
	// }
	//
	// }else{
	// //didn't need to convert the postgres one, but mysql is
	// //returning java.sql.timestamp, which, surprise surprise
	// //is another thing that breaks GWT serialization.
	// //
	// log.debug("upd "+t.getLastUpdated());
	// log.debug("cre "+t.getCreated());
	// if(t.getLastUpdated() != null)
	// t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
	// if(t.getCreated() != null)
	// t.setCreated(new Date(t.getCreated().getTime()));
	//
	// log.debug("starting convert sets");
	// log.debug("SIZE: "+t.getTypes().size());
	// t.setScopes(new HashSet());
	// t.setTypes(converter(t.getTypes(),level,false,true));
	//
	// log.debug("starting convert sets-instances");
	// //t.setInstances(converter(t.getInstances(),level));
	//
	// log.debug("starting convert sets-occurrences");
	// t.setOccurences(converterOccurenceSet(t.getOccurences()));
	//
	// log.debug("starting convert sets-assocations");
	// t.setAssociations(converter(t.getAssociations(),level,true));
	//
	// if(t.getSubject() != null){
	// t.getSubject().setTopics(new HashSet());
	// }
	// }
	// log.debug("Finally: t "+t.getId()+" "+t.getUser());
	//
	// try{
	// log.debug("Scan turned up persistent: "+Converter.scan(t));
	// }catch(Exception e){
	// log.error("Scanning error "+e);
	// e.printStackTrace();
	// }
	//
	// return t;
	// }
	//
	// public static Set converter(Set in,int level){
	// return converter(in, level,false);
	// }
	// public static Set converter(Set in,int level,boolean hasMembers){
	// return converter(in,level,hasMembers,false);
	// }
	// public static Set converter(Set in,int level,boolean hasMembers,boolean
	// typesWithAssociations){
	// HashSet<Topic> rtn = new HashSet<Topic>();
	// try{
	// log.debug("converter "+in+" "+rtn+" level: "+level);
	// for (Iterator iter = in.iterator(); iter.hasNext();) {
	// Topic top = (Topic) iter.next();
	// log.debug("converter on "+top);
	// convert(top,level+1,hasMembers,typesWithAssociations);
	// log.debug("converted "+top);
	//
	// rtn.add(top);
	// }
	// }catch(LazyInitializationException ex){
	// log.debug("caught lazy @ level "+level);
	// }
	// return rtn;
	// }



	public static AbstractTopic convertFull(AbstractTopic topic) {
		topic.setSubject(simple(topic.getSubject()));

		topic.setTypes(tags(topic.getTypes()));
		topic.setAssociations(assocSet(topic.getAssociations()));
		// topic.setOccurences(simpleSet(topic.getOccurenceObjs()));

		return topic;
	}


	private static Set assocSet(Set<Association> associations) {
		Set<Association> rtn = new HashSet<Association>();

		for (Association association : associations) {
			association.setMembers(simpleSet(association.getMembers()));
			association.setTypes(simpleTags(association.getTypes()));
			rtn.add(association);
		}

		return rtn;
	}

	private static Association assoc(Association assoc) {

		return assoc;
	}

	private static Set simpleSet(Set topics) {
		HashSet<Topic> rtn = new HashSet<Topic>();

		return null;
	}

	private static Set simpleTags(Set<TopicTypeConnector> types) {
		// TODO Auto-generated method stub
		return null;
	}

	private static Set tags(Set<TopicTypeConnector> types) {
		HashSet<TopicTypeConnector> rtn = new HashSet<TopicTypeConnector>();
		for (TopicTypeConnector connector : rtn) {
			// connector.setTopic(
			// connector.setType(type)
		}
		return rtn;
	}



	private static Subject simple(Subject s) {
		s.setTopics(null);
		return s;
	}

	private static Topic simple(Topic t) {
		if (t.getLastUpdated() != null)
			t.setLastUpdated(new Date(t.getLastUpdated().getTime()));
		if (t.getCreated() != null)
			t.setCreated(new Date(t.getCreated().getTime()));

		t.setSubject(null);
		t.setScopes(new HashSet());
		t.setInstances(new HashSet());
		t.setOccurences(new HashSet());
		t.setAssociations(new HashSet());
		t.setTypes(new HashSet());

		return t;
	}



}