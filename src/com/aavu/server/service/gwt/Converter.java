package com.aavu.server.service.gwt;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.generated.AbstractTopic;

public class Converter {
	private static final Logger log = Logger.getLogger(Converter.class);

	private Object originalCaller;
	private Object valueForCaller;

	public static boolean scan(AbstractTopic object){
		
		Class  objectClass = object.getClass();
		while(!objectClass.getSuperclass().getName().contains("Object")){
			objectClass = objectClass.getSuperclass();
		}
		
		
		Method[] methods=objectClass.getDeclaredMethods();
		
		
		String className = object.getClass().getName();
		if(className.contains("Persistent")){
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
	
			
			//System.out.println("method "+methodReturnType);
			//System.out.println("param length"+method.getGenericParameterTypes().length);
			if(methodReturnType == java.util.Set.class
					&&
					method.getGenericParameterTypes().length == 0){
				
				
				log.debug("Examine method "+method.getName());
			
				//System.out.println("implement "+methodReturnType+"   "+implementsCollection(methodReturnType));			
								
				Collection<AbstractTopic> sourceCollection = null;
				try {
					sourceCollection = (Collection)method.invoke(object,null);
				} catch (Exception e) {
					e.printStackTrace();
				} 

				//System.out.println("rtn: "+sourceCollection);
				//System.out.println(sourceCollection.getClass());
				
				if(sourceCollection.getClass().toString().contains("Persist")){
					log.error("RETURNING A HIBERNATE CLASS!!!!!!!!!!");
					b = true;
				}
				
				for (AbstractTopic topic : sourceCollection) {
					log.debug("INSIDE!!!!!!!!!!!!!!!!!!!!!! ");
					if(scan(topic)){
						log.warn("Field: "+method.getName()+" was Hibernate.");
						b = true;
					}	
				}
				
			}
		}
		
		return b;
		
	}
	
	public Object convert(Object object){

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

			for(int i=0;i<fields.length;i++){
				//                      log.info(""+fields[i].getClass().isPrimitive());
			}

			for(int i=0;i<methods.length;i++){
				try{
					if(!methods[i].getName().startsWith("get"))
						continue;

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

	private boolean isPrimitive(Class objectClass){
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

	private boolean isDate(Class objectClass){
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

	private Date convertToDate(Date date){
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

	private List convertToList(Collection collection){
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