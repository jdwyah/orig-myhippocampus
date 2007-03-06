package com.aavu.client.domain.commands;

import java.util.Set;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoBusinessException;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RemoveTagFromTopicCommand extends AbstractSaveCommand implements IsSerializable {

	public RemoveTagFromTopicCommand(){};
	
	public RemoveTagFromTopicCommand(Topic topic, Topic tag){
		super(topic,tag);		
	}

	//@Override
	public void executeCommand() throws HippoBusinessException {
		
		if(!(getTopic(1) instanceof Tag)){
			throw new HippoBusinessException("Can't remove nontag: "+getTopic(1));
		}
		
		boolean res = getTopic(0).removeTag((Tag) getTopic(1));
		if(!res){								
			throw new HippoBusinessException("Error Removing Tag");
		}				
	}

	//@Override
	public String toString() {
		return "SaveTagToTopic ID "+getTopicID(0)+" "+getTopicID(1);
	}


	//@Override
	public Set getAffectedTopics() {
		Set s = super.getAffectedTopics();
		s.add(getTopic(1));
		return s;
	}
	
	
	
}
