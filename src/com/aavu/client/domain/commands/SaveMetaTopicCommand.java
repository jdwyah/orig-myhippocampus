package com.aavu.client.domain.commands;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.Meta;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaTopicCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveMetaTopicCommand(){};
		
	public SaveMetaTopicCommand(long id, long metaid, long topic2id){
		setTopicID(id);
		setId1(metaid);
		setId2(topic2id);
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
