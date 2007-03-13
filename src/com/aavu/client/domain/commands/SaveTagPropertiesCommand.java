package com.aavu.client.domain.commands;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTagPropertiesCommand extends AbstractCommand implements IsSerializable {

	private Meta[] metas;
	
	public SaveTagPropertiesCommand(){};
	
	public SaveTagPropertiesCommand(Topic topic, Meta[] metas){
		super(topic);
		this.metas = metas;		
	}

	//@Override
	public void executeCommand() {
				
		//TODO BOGUS
		getTopic(0).getTagProperties().clear();
		
		for (int i = 0; i < metas.length; i++) {
			if(metas[i].getTitle() != null 
					&&
			   !metas[i].getTitle().equals("")){
			
				getTopic(0).addTagProperty(metas[i]);
			}						
		}
				
	}

	//@Override
	public String toString() {
		return "SaveTagProperties ID "+getTopicID(0)+" "+getData();
	}
	
	
	
}
