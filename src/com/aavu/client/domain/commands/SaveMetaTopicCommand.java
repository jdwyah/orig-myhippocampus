package com.aavu.client.domain.commands;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaTopicCommand extends AbstractCommand implements IsSerializable {

	public SaveMetaTopicCommand(){};

	public SaveMetaTopicCommand(Topic topic, Topic meta, Topic topic2){
		super(topic,meta,topic2);		
	}

	//@Override
	public void executeCommand() {		
		getTopic(0).addMetaValue((Meta) getTopic(1), getTopic(2));
	}

	//@Override
	public String toString() {
		return "SaveMetaTopic ID "+getTopicID(0)+" "+getTopicID(1)+" "+getTopicID(2);
	}
	
	
	
	
}
