package com.aavu.client.domain.commands;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Carry along the HippoDate to store. On execute, see if there's already something 
 * saved for this Meta, is so overwrite it with copyPropsButNotID(). This enforces our
 * 'only-one-meta-date-per-meta.' 
 * 
 * NOTE: delete will require a separate command.
 * 
 * @author Jeff Dwyer
 *
 */
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
			mv.setUser(getTopic(0).getUser());
			System.out.println("SaveMetaDateCommand. Didn't exist. Adding.");
			getTopic(0).addMetaValue((Meta) getTopic(1), value);
		}else{
			System.out.println("SaveMetaDateCommand. Already had a metavalue, copyPropsButNotID.");
			value.copyPropsButNotIDIntoParam(mv);			
		}
				
	}

	//@Override
	public String toString() {
		return "SaveDate ID "+getTopicID(0)+" "+value;
	}
	
	
	
}
