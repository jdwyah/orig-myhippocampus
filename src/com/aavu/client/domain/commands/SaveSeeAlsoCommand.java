package com.aavu.client.domain.commands;

import com.aavu.client.domain.MetaSeeAlso;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveSeeAlsoCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveSeeAlsoCommand(){};
	
	public SaveSeeAlsoCommand(long id, long seealsoid){
		setTopicID(id);
		setId1(seealsoid);		
	}

	//@Override
	public void executeCommand() {						
		topic.addSeeAlso(topic1,(MetaSeeAlso) topic2);
	}

	//@Override
	public String toString() {
		return "SaveSeeAlso ID "+getTopicID()+" "+getId1();
	}
	
	
	
}
