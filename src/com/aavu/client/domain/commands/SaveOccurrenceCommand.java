package com.aavu.client.domain.commands;

import java.util.Iterator;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveOccurrenceCommand extends AbstractSaveCommand implements IsSerializable {

	private Occurrence occurrence;
	
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
			for (Iterator iter = topic.getOccurences().iterator(); iter.hasNext();) {
				Occurrence occur = (Occurrence) iter.next();
				if(occur.getId() == occurrence.getId()){
										
					existing = occur;
					break;
					
					//iter.remove();					
					
					//NOTE don't use this, ConcurrentModificationDanger
					//topic.getOccurences().remove(occur);					
				}
			}			
			
			if(existing != null){
				occurrence.copyProps(existing);
			}else{
				topic.getOccurences().add(occurrence);
			}
			
		}else{				
			topic.getOccurences().add(occurrence);
		}
	}

	//@Override
	public String toString() {
		return "SaveOccurrence ID "+getTopicID()+" "+occurrence;
	}
	
	
	
}
