package com.aavu.client.domain.commands;

import java.util.Set;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTitleCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveTitleCommand(){};
	
	public SaveTitleCommand(Topic topic, String title){
		super(topic);
		setData(title);
	}

	//@Override
	public void executeCommand() {
		getTopic(0).setTitle(getData());
	}

	//@Override
	public String toString() {
		return "SaveTitle ID "+getTopicID(0)+" "+getData();
	}

	//@Override
	public boolean updatesTitle() {	
		return true;
	};
	
	

	/**
	 * update all tags that contain this topic 
	 */
	//@Override
	public Set getAffectedTopics() {		
		return getTopic(0).getTypesAsTopics();
	}
	
	
	
	
}
