package com.aavu.client.domain.commands;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveEntryTextCommand extends AbstractSaveCommand implements IsSerializable {

	public SaveEntryTextCommand(){};
	
	public SaveEntryTextCommand(long id, String text){
		setTopicID(id);
		setData(text);
	}

	//@Override
	public void executeCommand() {					
		topic.getLatestEntry().setData(getData());		
	}

	//@Override
	public String toString() {
		return "SaveText ID "+getTopicID()+" "+getData();
	}
	
	
	
}
