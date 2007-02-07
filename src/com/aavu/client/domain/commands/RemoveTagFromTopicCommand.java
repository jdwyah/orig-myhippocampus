package com.aavu.client.domain.commands;

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
		
		if(!(topic1 instanceof Tag)){
			throw new HippoBusinessException("Can't remove nontag: "+topic1);
		}
		
		boolean res = topic.removeTag((Tag) topic1);
		if(!res){								
			throw new HippoBusinessException("Error Removing Tag");
		}				
	}

	//@Override
	public String toString() {
		return "SaveTagToTopic ID "+getTopicID()+" "+getId1();
	}
	
	
	
}
