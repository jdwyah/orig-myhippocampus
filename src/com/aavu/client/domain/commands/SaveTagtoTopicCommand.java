package com.aavu.client.domain.commands;

import java.util.Set;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoBusinessException;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTagtoTopicCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveTagtoTopicCommand(){};
	
	public SaveTagtoTopicCommand(Topic topic, Tag tag){
		super(topic,tag);
	}

	//@Override
	public void executeCommand() throws HippoBusinessException {
				
		if(!(getTopic(1) instanceof Tag)){
			throw new HippoBusinessException("Can't tag with nontag: "+getTopic(1));
		}
		
		getTopic(0).tagTopic(getTopic(1));		
		
			
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
