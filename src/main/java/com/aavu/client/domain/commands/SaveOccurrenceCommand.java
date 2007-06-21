package com.aavu.client.domain.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.util.SetUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveOccurrenceCommand extends AbstractCommand implements IsSerializable {

	private Occurrence occurrence;
	private int removeStartNumber;
	private transient Set affected = new HashSet();
	
	public SaveOccurrenceCommand(){};
	
	public SaveOccurrenceCommand(List topics, Occurrence occurrence){
		this(topics,occurrence,-1);
	}
	
	public SaveOccurrenceCommand(List topics, Occurrence occurrence,int removeStartNumber){
		super(topics);
		this.occurrence = occurrence;
		this.removeStartNumber = removeStartNumber;
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
			
			List newTopics = new ArrayList();
			
			for (Iterator iter = getAddTopics().iterator(); iter.hasNext();) {
				Topic topic = (Topic) iter.next();
				
				//temp, make sure to set higher scoped 'existing'
				Occurrence exhist = (Occurrence) SetUtils.getFromSetById(topic.getOccurenceObjs(), occurrence.getId());				
				
				if(exhist != null){
					
					existing = exhist;
					
					System.out.println("Exist "+topic);
					occurrence.copyPropsIntoParam(existing);
				}else{
					//can't simply add here, because that will NonUnique (existing && occurence)
					//add later, because something should have an existing
					System.out.println("NoExisting "+topic);
					newTopics.add(topic);					
				}
				
			}
			
			//Add to topics that didn't already have it
			//
			if(!newTopics.isEmpty()){
				for (Iterator iter = newTopics.iterator(); iter.hasNext();) {
					Topic topic = (Topic) iter.next();
					if(existing != null){
						topic.addOccurence(existing);
					}else{
						System.out.println("SaveOccurrenceCommand WARN No existing occurrence");
						topic.addOccurence(occurrence);
					}
					occurrence.getTopics().add(topic);
				}
			}
			
			
			System.out.println("Do Remove");		
			for (Iterator iter = getRemoveItems().iterator(); iter.hasNext();) {
				Topic inLink = (Topic) iter.next();
				System.out.println("still has link"+inLink);
				Occurrence exist2 = (Occurrence) SetUtils.getFromSetById(inLink.getOccurenceObjs(), occurrence.getId());
				boolean r1 = inLink.getOccurenceObjs().remove(exist2);								
				boolean r2 = occurrence.getTopics().remove(inLink);
				if(!(r1 && r2)){
					System.out.println("WARN SaveOccurrence Not Removing "+r1+" "+r2);
				}
			}
			
		}else{			
			for (Iterator iter = getAddTopics().iterator(); iter.hasNext();) {
				Topic topic = (Topic) iter.next();
				topic.addOccurence(occurrence);	
				
				occurrence.getTopics().add(topic);
			}			
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
//				topic.addOccurrence(occurrence);				
//			}			
//			else{
//				System.out.println("Contained. do nothing");
//			}
//			affected.add(topic);
//		}		
	}
		
	public List getRemoveItems() {		
		if(removeStartNumber == -1){
			return new ArrayList();
		}
		//return getTopics().subList(removeStartNumber, getTopics().size());
		return subList(getTopics(), removeStartNumber, getTopics().size());
	}

	public List getAddTopics() {
		if(removeStartNumber == -1){
			return getTopics();
		}
		//return getTopics().subList(0, removeStartNumber);
		return subList(getTopics(), 0, removeStartNumber);
	}
	
	//TODO java 1.5 makes this unec
	private List subList(List l, int s, int e){
		List rtn  = new ArrayList();
		for(int i = s; i < e; i++){
			rtn.add(l.get(i));
		}
		return rtn;
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
		return "SaveOccurrence ID "+getTopicID(0)+" "+occurrence;
	}

	public Occurrence getOccurrence() {
		return occurrence;
	}
	
	
	
}
