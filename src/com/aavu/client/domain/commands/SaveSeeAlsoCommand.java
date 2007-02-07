package com.aavu.client.domain.commands;

import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveSeeAlsoCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveSeeAlsoCommand(){};
	
	public SaveSeeAlsoCommand(Topic topic, Topic seealso){
		super(topic,seealso);		
		
		//set this transient guy here. Server will reset this with an ugly:
		//instance of SeeAlso to make sure we get our singleton.
		topic2 = new MetaSeeAlso();
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
