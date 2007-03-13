package com.aavu.client.domain.commands;

import java.util.Set;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoBusinessException;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RemoveMetaFromTopicCommand extends AbstractCommand implements IsSerializable {

	public RemoveMetaFromTopicCommand(){};
	
	public RemoveMetaFromTopicCommand(Topic topic, Meta meta){
		super(topic,meta);		
	}

//	@Override
	public void executeCommand() throws HippoBusinessException {
		
		if(!(getTopic(1) instanceof Meta)){
			throw new HippoBusinessException("Can't remove non-meta: "+getTopic(1));
		}
		
		boolean res = getTopic(0).removeMeta((Meta) getTopic(1));
		if(!res){								
			throw new HippoBusinessException("Error Removing Meta");
		}				
	}

	//@Override
	public String toString() {
		return "RemoveMetaFromTopicCommand ID "+getTopicID(0)+" "+getTopicID(1);
	}


	//@Override
	public Set getAffectedTopics() {
		Set s = super.getAffectedTopics();
		s.add(getTopic(1));
		return s;
	}
	
	
	
}
