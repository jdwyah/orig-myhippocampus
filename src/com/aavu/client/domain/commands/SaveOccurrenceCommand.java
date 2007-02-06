package com.aavu.client.domain.commands;

import java.util.Iterator;

import com.aavu.client.domain.Occurrence;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveOccurrenceCommand extends AbstractSaveCommand implements IsSerializable {

	private Occurrence occurrence;
	
	public SaveOccurrenceCommand(){};
	
	public SaveOccurrenceCommand(long id, Occurrence occurrence){
		setTopicID(id);
		this.occurrence = occurrence;
	}

	//@Override
	public void executeCommand() {
		
		if(occurrence.getId() != 0){					
			for (Iterator iter = topic.getOccurences().iterator(); iter.hasNext();) {
				Occurrence occur = (Occurrence) iter.next();
				if(occur.getId() == occurrence.getId()){
					topic.getOccurences().remove(occur);					
				}
			}
		}
		topic.getOccurences().add(occurrence);				
	}

	//@Override
	public String toString() {
		return "SaveOccurrence ID "+getTopicID()+" "+occurrence;
	}
	
	
	
}
