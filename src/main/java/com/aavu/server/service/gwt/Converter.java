package com.aavu.server.service.gwt;

import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.generated.AbstractTopic;
import com.aavu.client.domain.generated.AbstractTopicTypeConnector;
import com.aavu.server.util.gwt.ServerSerializationStreamWriterWithHibernateEscaping;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.impl.ServerSerializableTypeOracle;
import com.google.gwt.user.server.rpc.impl.ServerSerializableTypeOracleImpl;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;

public class Converter {
	private static final Logger log = Logger.getLogger(Converter.class);

	/**
	 * NOTE!! this is a fragile thing. What if they have a topic named CGLIB!? 
	 * 
	 * Treat this as a useful diagnostic only!
	 * 
	 * @param object
	 * @return
	 */
	public static boolean scan(Object object){
		log.debug("scanning with hibernate support ");
		String str = serializeWithHibernateSupport(object);
		
		log.debug("scanning REGULAR");
		serialize(object);
		
		
		log.info("CGLib: "+str.contains("CGLIB")+
				" Persistent "+str.contains("Persistent")+
				" Timestamp "+str.contains("java.sql.Timestamp")+
				" EX: "+str.contains("{EX}"));
		
		return str.contains("CGLIB")
		||
		str.contains("Persistent")
		||
		str.contains("java.sql.Timestamp");
	}
	
	public static String serialize(Object t){
		ServerSerializableTypeOracle serializableTypeOracle;
		serializableTypeOracle = new ServerSerializableTypeOracleImpl(
				getPackagePaths());
		ServerSerializationStreamWriter stream = new ServerSerializationStreamWriter(
				serializableTypeOracle);
		

		stream.prepareToWrite();

		Object responseObj = t;
		boolean isException = false;
		try {
			stream.serializeValue(responseObj, Topic.class);
		} catch (SerializationException e) {
			responseObj = e;
			isException = true;
		}
		String bufferStr = (isException ? "{EX}" : "{OK}") + stream.toString();
		System.out.println("REG "+bufferStr);
		return bufferStr;
	}
	public static String serializeWithHibernateSupport(Object t){
		ServerSerializableTypeOracle serializableTypeOracle;
		serializableTypeOracle = new ServerSerializableTypeOracleImpl(
				getPackagePaths());
		ServerSerializationStreamWriterWithHibernateEscaping stream = new ServerSerializationStreamWriterWithHibernateEscaping(
				serializableTypeOracle);
		

		stream.prepareToWrite();

		Object responseObj = t;
		boolean isException = false;
		try {
			stream.serializeValue(responseObj, t.getClass());
		} catch (SerializationException e) {
			responseObj = e;
			isException = true;
		}
		String bufferStr = (isException ? "{EX}" : "{OK}") + stream.toString();
		System.out.println("HIBSUPPORT "+bufferStr);
		return bufferStr;
	}

	
	private static String[] getPackagePaths() {
	    return new String[] {"com.google.gwt.user.client.rpc.core"};
	  }
	
	
	public static boolean oldScanner(AbstractTopic object){

		Class  objectClass = object.getClass();
		while(!objectClass.getSuperclass().getName().contains("Object")){
			objectClass = objectClass.getSuperclass();
		}


		Method[] methods=objectClass.getDeclaredMethods();


		String className = object.getClass().getName();
		if(className.contains("Persistent")){
			log.warn("Persistent");
			return true;
		}
		if(className.contains("CGLIB")){
			log.warn("CGLIB");
			return true;
		}
		log.debug("classname "+className);

		boolean b = false;


		for (Method method : methods) {

			//System.out.println("method "+method.getName());

			if(!method.getName().startsWith("get"))
				continue;

			//get return type of a method
			Class methodReturnType=method.getReturnType();


			//System.out.println("method "+method.getName()+" rtn: "+methodReturnType);
			//System.out.println("param length"+method.getGenericParameterTypes().length);
			if(methodReturnType == java.util.Set.class
					&&
					method.getGenericParameterTypes().length == 0){


				log.debug("Examine method "+method.getName());

				//System.out.println("implement "+methodReturnType+"   "+implementsCollection(methodReturnType));			

				Collection<Object> sourceCollection = null;
				try {
					sourceCollection = (Collection)method.invoke(object,null);
				} catch(IllegalAccessException ee){
					log.debug("Method: "+method.getName()+" is not accessible.");					
				} catch (Exception e) {
					e.printStackTrace();					
				} 

				if(sourceCollection != null){

					//System.out.println("rtn: "+sourceCollection);
					//System.out.println(sourceCollection.getClass());

					if(sourceCollection.getClass().toString().contains("Persist")){
						log.error(method.getName()+" is RETURNING A HIBERNATE CLASS!!!!!!!!!!");
						b = true;
					}

					try{
						
						for (Object obj : sourceCollection) {							
							log.debug("moving into set____________");
							if(obj instanceof AbstractTopic){
								if(scan((AbstractTopic) obj)){
									log.warn("Topic in "+method.getName()+" was Hibernate!!!!!!!!!!!!!!");
									b = true;
								}	
							}
							else if(obj instanceof AbstractTopicTypeConnector){
								AbstractTopicTypeConnector conn = (AbstractTopicTypeConnector) obj;
								if(scan(conn.getType())){
									log.warn("Field: "+method.getName()+" was Hibernate!!!!!!!!!!!!");
									b = true;
								}	
							}else {
								log.warn("unknown "+obj.getClass());
							}
						}
					}catch(ClassCastException c){
						//lazy code above, some of these will now be occurrence, not topics
						log.debug("cast error: "+c);
					}
				}
			}
		}

		return b;

	}

}