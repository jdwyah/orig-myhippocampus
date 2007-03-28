package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class FTICachingExplorerPanel extends Composite implements ExplorerPanel {

	//public FTICachingExplorerPanel
	
	
	private static Map tagToIdentifierMap;
	private Manager mananager;
	protected boolean allMode;	
	protected Set tags;
	
	public FTICachingExplorerPanel(Manager manager, Map existingMap){
		tagToIdentifierMap = existingMap;
		this.mananager = manager;  
	}
	
	
	public Widget getWidget() {		
		return this;
	}

	protected List makeShoppingList(final Set tags){
		List shoppingList = new ArrayList();
		
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			TopicIdentifier tag = (TopicIdentifier) iter.next();
			
			
			System.out.println("check "+tag);
			if(!tagToIdentifierMap.containsKey(tag)){		
				System.out.println("adding "+tag+" to shopping list.");
				shoppingList.add(tag);
			}			
		}
		return shoppingList;
	}
	
	public void load(final Set tags) {
		this.tags = tags;
		allMode = false;
		
		System.out.println("ftu load "+tags.size());
		
		
		
		//doload		
		final List shoppingList = makeShoppingList(tags);
		
		if(!shoppingList.isEmpty()){
			
			System.out.println("Going shopping");
			
			mananager.getTopicCache().getTopicsWithTag(shoppingList, 
					new StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){
						//@Override
						public void onSuccess(Object result) {
							super.onSuccess(result);
							System.out.println("Shopping list back "+result);
							int count = 0; 
							
							//List<List<FullTopicIdentifier>>
							List listOfListOfFTIs = (List) result;
							for (Iterator iter = listOfListOfFTIs.iterator(); iter.hasNext();) {
								List ftis = (List) iter.next();
								
								TopicIdentifier tagIdent = (TopicIdentifier) shoppingList.get(count);
								
								System.out.println("inserting "+tagIdent+" "+ftis);
								
								tagToIdentifierMap.put(tagIdent, ftis);
								
								count++;
							}
							
							drawTags(tags);
						}});
		}else{
			System.out.println("no shopping needed");
			
			System.out.println("draw(tags)");
			drawTags(tags);
		}

	}
	
	public void loadAll() {
		allMode = true; 
		mananager.getTopicCache().getAllTopicIdentifiers( 
				new StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){
					//@Override
					public void onSuccess(Object result) {
						super.onSuccess(result);
											
						draw((List) result);					
					}});
	}

	protected List getFTI(TopicIdentifier tag) {
		return (List) tagToIdentifierMap.get(tag);
	}
		
	protected void drawTags(Set tags) {
		List all = new ArrayList();		
		for (Iterator iter = tags.iterator(); iter.hasNext();) {			
			TopicIdentifier tag = (TopicIdentifier) iter.next();
		
			System.out.println("draw(tags) get("+tag+") = "+getFTI(tag));
			
			all.addAll(getFTI(tag));
		}
		draw(all);
	} 

	
	protected abstract void draw(List ftis);

}
