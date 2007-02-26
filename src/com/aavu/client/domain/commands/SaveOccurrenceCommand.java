package com.aavu.client.domain.commands;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveOccurrenceCommand extends AbstractSaveCommand implements IsSerializable {

	private Occurrence occurrence;
	private transient Set affected = new HashSet();
	
	public SaveOccurrenceCommand(){};
	
	public SaveOccurrenceCommand(Topic topic, Occurrence occurrence){
		super(topic);
		this.occurrence = occurrence;
	}

	//@Override
	/**
	 * This is a bit sqirrley. We can't do a get() on the set, since 
	 * .equals() compares the data & won't find the match. 
	 * 
	 * Use copyProps instead of remove/add to avoid 
	 * 'Object with ID already associated with session' type errors.
	 * 
	 * PEND MED. Make this less ugly.
	 */
	public void executeCommand() {
		
		if(occurrence.getId() != 0){		
			Occurrence existing = null;
//			for (Iterator iter = topic.getOccurences().iterator(); iter.hasNext();) {
//				Occurrence occur = (Occurrence) iter.next();
//				if(occur.getId() == occurrence.getId()){
//										
//					existing = occur;
//					break;
//					
//					//iter.remove();										
//					//NOTE don't use this, ConcurrentModificationDanger
//					//topic.getOccurences().remove(occur);					
//				}
//			}			
			existing = (Occurrence) getFromSetById(topic.getOccurences(), occurrence.getId());
			
			if(existing != null){
				occurrence.copyProps(existing);
			}else{
				topic.getOccurences().add(occurrence);
			}
			
		}else{				
			topic.getOccurences().add(occurrence);
		}
		
		//System.out.println("LOOPING over "+occurrence+" topics");
		
		
		/**
		 * loop over all the topics that the occurrence says it's related too.
		 * If those topics don't have this occurence already, add it.
		 * 
		 * Can't do a basic contains() since we're .equals is not operating on ID
		 */
//		for (Iterator iter = occurrence.getTopics().iterator(); iter.hasNext();) {
//			Topic topic = (Topic) iter.next();
//			System.out.println("found "+topic);
//			
//			if(null == getFromSetById(topic.getOccurences(), occurrence.getId())){
//				System.out.println("Didn't contain "+occurrence+" ADD");		
//				topic.getOccurences().add(occurrence);				
//			}			
//			else{
//				System.out.println("Contained. do nothing");
//			}
//			affected.add(topic);
//		}		
	}
	
	private Object getFromSetById(Set topics,long id){
		for (Iterator iter = topics.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			if (o instanceof Occurrence) {
				Occurrence occ = (Occurrence) o;
				if(occ.getId() == id){
					return occ;
				}
			}
			if (o instanceof Topic) {
				Topic top = (Topic) o;
				if(top.getId() == id){
					return top;
				}
			}			
		}			
		return null;
	}
	
	/**
	 * make sure we save the other topics that we may have added ourselves to,
	 * since this is the inverse side of the relationship
	 */
	//@Override
	public Set getAffectedTopics() {
		Set s = super.getAffectedTopics();
		s.addAll(affected);
		return s;
	}
	
	
	//@Override
	public String toString() {
		return "SaveOccurrence ID "+getTopicID()+" "+occurrence;
	}
	
	
	
}
