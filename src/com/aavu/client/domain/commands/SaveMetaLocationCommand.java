package com.aavu.client.domain.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.HippoText;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Save a List of HippoLocations. Must pass all locations for the given Meta
 * 
 * @author Jeff Dwyer
 *
 */
public class SaveMetaLocationCommand extends AbstractCommand implements IsSerializable {

	private Set values;

	public SaveMetaLocationCommand(){};
	
	public SaveMetaLocationCommand(Topic topic, Meta meta, Set values){
		super(topic,meta);
		this.values = values;
	}

	//@Override
	public void executeCommand() {

//		HippoLocation mv = (HippoLocation) getTopic(0).getSingleMetaValueFor((Meta) getTopic(1));
//
//		HippoLocation incoming = (HippoLocation)values.iterator().next();
//		if(mv == null){
//			mv = new HippoLocation();		
//		}
//		mv.setUser(getTopic(0).getUser());
//		mv.setTitle(incoming.getTitle());
//		mv.setLatitude(incoming.getLatitude());
//		mv.setLongitude(incoming.getLongitude());
//		
//		getTopic(0).addMetaValue((Meta) getTopic(1), mv);		
		
//		org.hibernate.NonUniqueObjectException: a different object with the same identifier value was already associated
//		with the session: [com.aavu.client.domain.MetaLocation#2457]
		//over-write all locations
		//clear on first iteration, then don't clear
		
		System.out.println("\nEXECUTE SAVE META");
		System.out.println("values "+values.size());
		
		boolean clear = true;
		for (Iterator iter = values.iterator(); iter.hasNext();) {
			HippoLocation location = (HippoLocation) iter.next();
			System.out.println("----"+location);
			location.setUser(getTopic(0).getUser());
			getTopic(0).addMetaValue((Meta) getTopic(1), location,clear);	
			
			clear = false;
		}
		
		System.out.println("topic assoc "+getTopic(0).getAssociations().size());
		System.out.println("topic metas "+getTopic(0).getMetas().size());
		
		//System.out.println(" "+getTopic(0).toPrettyString());
		
		//would like to use code like this, but since we're not returning ids,
		//it won't work right.
		//
//		//		if unsaved, just save
//		//
//		//else, copy the value of the location across
//		//
//		if(value.getId() == 0){
//			getTopic(0).addMetaValue((Meta) getTopic(1), value,false);	
//		}else{
//			Set metaValues = getTopic(0).getMetaValuesFor((Meta) getTopic(1));
//			HippoLocation savedLocation = (HippoLocation) SetUtils.getFromSetById(getAffectedTopics(), value.getId());
//			savedLocation.setLocation(value.getLocation());			
//		}
	}

	
	public List getTopics() {
		System.out.println("\n\n\nRETURN SMALL SUBLIST");
		
		List rtn = new ArrayList();
		rtn.add(topics.get(0));
		
		return rtn;
		//return topics.subList(0, 1);
	}
	
	//@Override
	public String toString() {
		return "SaveLocationCommand "+getTopicID(0)+" "+getTopic(1)+" "+values;
	}
	
	
	
}
