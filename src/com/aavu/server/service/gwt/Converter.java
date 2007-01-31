package com.aavu.server.service.gwt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.aavu.client.domain.generated.AbstractTopic;
import com.aavu.client.domain.generated.AbstractTopicTypeConnector;

public class Converter {
	private static final Logger log = Logger.getLogger(Converter.class);

	private static Object originalCaller;
	private static Object valueForCaller;

	public static boolean scan(AbstractTopic object){

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

	private static void printAllFields(Object fromObj,Class objectClass,int ii) throws InstantiationException, IllegalAccessException{
		if(objectClass != null && ii < 20){
			
			//Class  objectClass = object.getClass();
			System.out.println("object class "+objectClass);
			Field[] fields = objectClass.getDeclaredFields();
			
			//objectClass.
//			Class  objectClass = object.getClass();
//			while(!objectClass.getSuperclass().getName().contains("Object")){
//				objectClass = objectClass.getSuperclass();
//			}
			
			System.out.println("Fields "+Arrays.deepToString(fields));
			for(int i=0;i<fields.length;i++){
				
				log.info("F "+fields[i].getName()+" "+fields[i].getType()+" "+fields[i].getClass()+" "+fields[i].getClass().isPrimitive());
				
				fields[i].setAccessible(true);
				
				Object value = fields[i].get(fromObj);
				System.out.println("VALUE "+value+" "+value.getClass().getName());
				
				if(value == null){
					System.out.println("null");
				}else if(value.getClass().getName().contains("Lazy")){
					System.out.println("lazy");					
				}else if(value.getClass().getName().contains("HibernateSet")){
					System.out.println("hibernate");
					Set cur = (Set) value;
					Set s = new HashSet();
					for (Object object : cur) {
						s.add(convert(object));
					}
				}else if(isPrimitive(value.getClass())){
					System.out.println("primitive");
				}else if(implementsCollection(value.getClass())){
					System.out.println("convert collection");
					fields[i].set(fromObj, convert(value));
				}
			}
			
			System.out.println("super "+objectClass.getSuperclass());
			printAllFields(fromObj,objectClass.getSuperclass(),++ii);
		}
	}

	public static Object convert(Object object){

		Class  objectClass = object.getClass();
		String className = object.getClass().getName();
//		log.info(className);
		Object returnObject;

		try{
			returnObject = objectClass.newInstance();
			//              log.info("newInstance");
			Field[] fields=objectClass.getFields();
			//              log.info("getFields");
			Method[] methods=objectClass.getDeclaredMethods();
			//              log.info("getMethods");

		//	printAllFields(object,objectClass,0);

System.out.println("NUM FIELDS "+objectClass.getDeclaredFields().length);

			for(int i=0;i<methods.length;i++){
				try{
					if(!methods[i].getName().startsWith("get"))
						continue;
					

					
					System.out.println("DO METHOD: "+methods[i].getName());
					
					//get return type of a method
					Class methodReturnType=methods[i].getReturnType();

					String methodName=methods[i].getName();
					methodName="set"+methodName.substring(3);

					// if return type of the method is primitive then just call the
					//parallel set method for return object and pass it the value
					// produced by get method
					if(isPrimitive(methodReturnType)){
						Class[] paramType=new Class[1];
						paramType[0]=methodReturnType;
						Method method=returnObject.getClass().getMethod(methodName,paramType);

						Object param[]=new Object[1];
						param[0]=methods[i].invoke(object,null);
						method.invoke(returnObject,param);
					}
					else if(isDate(methodReturnType)){
						Class[] paramType=new Class[1];
						paramType[0]=methodReturnType;
						Method
						method=returnObject.getClass().getMethod(methodName,paramType);

						Object param[]=new Object[1];
						param[0]=convertToDate((Date)methods[i].invoke(object,null));
						method.invoke(returnObject,param);
					}
					else if(implementsCollection(methodReturnType)){
						Collection
						sourceCollection=(Collection)methods[i].invoke(object,null);

						Class[] paramType=new Class[1];
						paramType[0]=methodReturnType;
						Method method=returnObject.getClass().getMethod(methodName,paramType);

						Object param[]=new Object[1];
						originalCaller=object;
						valueForCaller=returnObject;
						param[0]=convertToList(sourceCollection);
						method.invoke(returnObject,param);
					}
					else{
						Class[] paramType=new Class[1];
						paramType[0]=methodReturnType;
						Method
						method=returnObject.getClass().getMethod(methodName,paramType);

						Object param[]=new Object[1];

						//get the value returned by get function
						Object arg=methods[i].invoke(object,null);

						//compare it with ur caller, if this is ur caller then update is needed
						if(originalCaller==arg)
							param[0]=valueForCaller;
						else{
							if(arg==null)
								param[0]=null;
							else{
								originalCaller=object;
								valueForCaller=returnObject;
								param[0]=convert(arg);
							}
						}
						method.invoke(returnObject,param);
					}
				}catch(Exception e){
					log.error("method fail : "+e.getCause()+":"+e.getMessage());
				}
			}

		}catch(Exception e){
			log.error("total converter(): "+e.getCause()+":"+e.getMessage());
			return null;
		}
		return returnObject;
	}

	private static boolean isPrimitive(Class objectClass){
		Logger log=Logger.getRootLogger();
//		log.debug("isPrimitive()");
		//log.debug(objectClass.toString());
		if(     objectClass.isPrimitive()||
				objectClass==Boolean.class||
				objectClass==Character.class||
				objectClass==Byte.class||
				objectClass==Short.class||
				objectClass==Integer.class||
				objectClass==Long.class||
				objectClass==Float.class||
				objectClass==Double.class||
				objectClass==String.class||
				objectClass==Void.class
		)
			return true;
		else
			return false;
	}

	private static boolean isDate(Class objectClass){
		if(objectClass==Date.class)
			return true;
		Class clas=objectClass;
		boolean isDate=false;
		do{
			if(clas==Date.class){
				isDate=true;
				break;
			}
			clas=clas.getSuperclass();
		}while(clas!=null);
		return isDate;
	}

	private static Date convertToDate(Date date){
		if(date==null)
			return null;
		return new Date(date.getTime());
	}

	private static boolean implementsCollection(Class objectClass){
		if(objectClass==Collection.class)
			return true;

		ArrayList interfaces=new ArrayList();
		Class superClass=objectClass;

		do{
			interfaces.add(Arrays.asList(superClass.getInterfaces()));
			superClass=superClass.getSuperclass();
		}while(superClass!=null);

		boolean isCollection=false;
		for(int j=0;j<interfaces.size();j++)
			if(interfaces.get(j)==Collection.class){
				isCollection=true;
				break;
			}
		return isCollection;
	}

	private static List convertToList(Collection collection){
		List targetList=new ArrayList();

		for(Object obj:collection.toArray())
			if(isPrimitive(obj.getClass()))
				targetList.add(obj);
			else if(implementsCollection(obj.getClass()))
				targetList.add(convertToList((List)obj));
			else{
				targetList.add(convert(obj));
			}
		return targetList;
	} 
}