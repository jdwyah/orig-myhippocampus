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
				
		if(!(topic1 instanceof Tag)){
			throw new HippoBusinessException("Can't tag with nontag: "+topic1);
		}
		
		topic.tagTopic(getTopic1());		
		
			
	}

	//@Override
	public String toString() {
		return "SaveTagToTopic ID "+getTopicID()+" "+getId1();
	}
	
	
	//@Override
	public Set getAffectedTopics() {
		Set s = super.getAffectedTopics();
		s.add(topic1);
		return s;
	}
	

}
