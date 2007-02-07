package com.aavu.client.domain.commands;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaTopicCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveMetaTopicCommand(){};

	public SaveMetaTopicCommand(Topic topic, Topic meta, Topic topic2){
		super(topic,meta,topic2);		
	}

	//@Override
	public void executeCommand() {		
		topic.addMetaValue((Meta) getTopic1(), getTopic2());
	}

	//@Override
	public String toString() {
		return "SaveMetaTopic ID "+getTopicID()+" "+getId1()+" "+getId2();
	}
	
	
	
	
}
