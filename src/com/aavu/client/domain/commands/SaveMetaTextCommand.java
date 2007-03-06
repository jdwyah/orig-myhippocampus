package com.aavu.client.domain.commands;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.HippoText;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaTextCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveMetaTextCommand(){};
	
	
	public SaveMetaTextCommand(Topic topic, Topic meta, String value){
		super(topic,meta);
		
		setData(value);
	}

	//@Override
	public void executeCommand() {

		HippoText mv = (HippoText) getTopic(0).getSingleMetaValueFor((Meta) getTopic(1));
		if(mv == null){
			mv = new HippoText(getData());
		}else{
			mv.setTitle(getData());
		}
		
		getTopic(0).addMetaValue((Meta) getTopic(1), mv);		

	}

	//@Override
	public String toString() {
		return "SaveText ID "+getTopicID(0)+" M:"+getTopicID(1)+" "+getData();
	}
	
	
	
}
