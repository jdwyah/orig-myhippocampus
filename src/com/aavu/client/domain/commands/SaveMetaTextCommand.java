package com.aavu.client.domain.commands;

import com.aavu.client.domain.HippoText;
import com.aavu.client.domain.Meta;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaTextCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveMetaTextCommand(){};
	
	public SaveMetaTextCommand(long id, long metaid, String value){
		setTopicID(id);
		setId1(metaid);
		setData(value);
	}

	//@Override
	public void executeCommand() {
		HippoText mv = new HippoText(getData());		
		topic.addMetaValue((Meta) getTopic1(), mv);		
	}

	//@Override
	public String toString() {
		return "SaveText ID "+getTopicID()+" M:"+getId1()+" "+getData();
	}
	
	
	
}
