package com.aavu.client.domain.commands;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaDateCommand extends AbstractCommand implements IsSerializable {

	public SaveMetaDateCommand(){};
	
	public SaveMetaDateCommand(Topic topic, Topic meta, String value){
		super(topic,meta);
		
		setData(value);
	}

	//@Override
	public void executeCommand() {
		HippoDate mv = (HippoDate) getTopic(0).getSingleMetaValueFor((Meta) getTopic(1));
		if(mv == null){
			mv = new HippoDate(getTopic(0).getUser(),getData());
		}else{
			mv.setTitle(getData());
		}
		getTopic(0).addMetaValue((Meta) getTopic(1), mv);		
	}

	//@Override
	public String toString() {
		return "SaveDate ID "+getTopicID(0)+" "+getData();
	}
	
	
	
}
