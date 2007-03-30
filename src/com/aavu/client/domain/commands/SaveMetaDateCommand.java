package com.aavu.client.domain.commands;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveMetaDateCommand extends AbstractCommand implements IsSerializable {

	private HippoDate value;
	
	public SaveMetaDateCommand(){};
	
	public SaveMetaDateCommand(Topic topic, Topic meta, HippoDate value){
		super(topic,meta);
		
		this.value = value;
	}

	//@Override
	public void executeCommand() {
		HippoDate mv = (HippoDate) getTopic(0).getSingleMetaValueFor((Meta) getTopic(1));
		if(mv == null){
			mv = value;
		}else{
			value.copyPropsButNotIDIntoParam(mv);			
		}
		
		System.out.println("DATE "+value);
		
		getTopic(0).addMetaValue((Meta) getTopic(1), value);		
	}

	//@Override
	public String toString() {
		return "SaveDate ID "+getTopicID(0)+" "+getData();
	}
	
	
	
}
