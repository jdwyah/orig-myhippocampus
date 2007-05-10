package com.aavu.client.domain.commands;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.util.SetUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTagPropertiesCommand extends AbstractCommand implements IsSerializable {

	private Meta[] metas;
	private transient Set noLongerPartOfUs = new HashSet();
	
	public SaveTagPropertiesCommand(){};
	
	public SaveTagPropertiesCommand(Topic topic, Meta[] metas){
		super(topic);
		this.metas = metas;		
	}

	//@Override
	public void executeCommand() {
				
		
		Association tagProperties = getTopic(0).getTagPropertyAssociation();
		
		//assume we'll remove all of them
		noLongerPartOfUs.addAll(tagProperties.getMembers());
		
		System.out.println("SaveTagProperties. Meta Length "+metas.length);
		for (int i = 0; i < metas.length; i++) {
			Meta meta = metas[i];
			
			System.out.println("SaveTagProperties.Meta "+meta);
			
			if(meta.getTitle() != null 
					&&
			   !meta.getTitle().equals("")){
			
				if(meta.getId() > 0){					
					System.out.println("SaveTagProperties.ID > 0 "+meta+" "+meta.getId());
					Meta saved = (Meta) SetUtils.getFromSetById(tagProperties.getMembers(), meta.getId());
										
					//this is ok. remember all metas should have IDs
					if(saved == null){
						System.out.println("SaveTagProperties.ADDING "+meta);
						tagProperties.getMembers().add(meta);
					}else{						
						meta.copyPropsIntoParam(saved);										
						SetUtils.removeFromSetById(noLongerPartOfUs, meta.getId());
					}
				}else{
					//Not really a reqirement, we could save these, but the code should never
					//try to add new Metas on the fly like this, so play it safe and error.
					throw new RuntimeException("Can't Save Unsaved Tag Properties");					
				}				
			}						
		}
		
		
		//need to explicitly remove things that we weren't told to save.
		//note that we definitely do NOT want to delete them. just remove
		//them from the member set.
		//
		for (Iterator iter = noLongerPartOfUs.iterator(); iter.hasNext();) {
			Meta meta = (Meta) iter.next();
			SetUtils.removeFromSetById(tagProperties.getMembers(), meta.getId());
		}
	
		
		//System.out.println("SaveTagProps finished. Will save:\n"+getTopic(0).toPrettyString());
		
	}


	//@Override
	public String toString() {
		return "SaveTagProperties ID "+getTopicID(0)+" "+metas;
	}
	
	
	
}
