package com.aavu.client.gui.glossary;

import java.util.Map;
import java.util.Set;

import com.aavu.client.gui.explorer.ExplorerPanel;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.service.Manager;

/**
 * Show all topics that aren't on an islands.
 * 
 * @author Jeff Dwyer
 *
 */
public class LostNFound extends Glossary implements ExplorerPanel {

	public LostNFound(Manager manager) {
		super(manager,Orientation.HORIZONTAL);
		
		
	}

	
//	//@Override
//	public void load(Map tagToIdentifierMap) {		
////		manager.getTopicCache().getAllTopicIdentifiers(new StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){
////
////			public void onSuccess(Object result) {
////				super.onSuccess(result);
////				List topics = (List) result;
////				load(topics);
////			}
////		});
//		
//	}
//
	
	public void load(Set tags) {
		// TODO Auto-generated method stub
		
	}


	public void loadAll() {
		// TODO Auto-generated method stub
		
	}

	
}
