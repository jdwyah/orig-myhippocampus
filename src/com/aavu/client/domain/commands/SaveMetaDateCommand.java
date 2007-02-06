package com.aavu.client.domain.commands;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.Meta;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaDateCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveMetaDateCommand(){};
	
	public SaveMetaDateCommand(long id, long metaid, String value){
		setTopicID(id);
		setId1(metaid);
		setData(value);
	}

	//@Override
	public void executeCommand() {
		HippoDate mv = new HippoDate(topic.getUser(),getData());		
		topic.addMetaValue((Meta) getTopic1(), mv);		
	}

	//@Override
	public String toString() {
		return "SaveDate ID "+getTopicID()+" "+getData();
	}
	
	
	
}
