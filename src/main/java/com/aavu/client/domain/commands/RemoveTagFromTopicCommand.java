package com.aavu.client.domain.commands;

import java.util.Set;

import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoBusinessException;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RemoveTagFromTopicCommand extends AbstractCommand implements IsSerializable {

	public RemoveTagFromTopicCommand(){};
	

	public RemoveTagFromTopicCommand(Topic topic, Topic tag){
		super(topic,tag);		
	}
	
	//@Override
	public void executeCommand() throws HippoBusinessException {
		
		
		boolean res = getTopic(0).removeType(getTopic(1));
		if(!res){								
			throw new HippoBusinessException("Error Removing Type");
		}				
	}

	//@Override
	public String toString() {
		return "RemoveTagFromTopicCommand ID "+getTopicID(0)+" "+getTopicID(1);
	}


	//@Override
	public Set getAffectedTopics() {
		Set s = super.getAffectedTopics();
		s.add(getTopic(0));
		return s;
	}
	
	
	
}
