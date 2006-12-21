package com.aavu.client.widget;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.LinkDisplayWidget;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.SeeAlsoBoard;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

public class AllReferencesPanel extends Composite {

	private SeeAlsoBoard seeAlsoBoard;
	private LinkDisplayWidget linkDisplayW;	
	
	private Manager manager;
	private FlowPanel refPanel;
	private StackPanel mainPanel;
	private int totalSize;
	
	/**
	 * Hold SeeAlsos, Links & References
	 * 
	 * @param manager
	 * @param saveNeeded
	 */
	public AllReferencesPanel(Manager manager, SaveNeededListener saveNeeded){
		
		this.manager = manager;
	
		mainPanel = new StackPanel();
		refPanel = new FlowPanel();
		
		
		seeAlsoBoard = new SeeAlsoBoard(manager,saveNeeded);		
		mainPanel.add(seeAlsoBoard,Manager.myConstants.seeAlsosN(0));				
		
		
		mainPanel.add(refPanel,Manager.myConstants.referencesN(0));
		
		
		linkDisplayW = new LinkDisplayWidget(manager,saveNeeded);
		mainPanel.add(linkDisplayW,Manager.myConstants.occurrencesN(0));		
				
		initWidget(mainPanel);
	}

	public void load(Topic topic, final TopicDetailsTabBar bar) {
		
		totalSize = 0;
		
		linkDisplayW.load(topic);
		updateTitle(linkDisplayW,Manager.myConstants.occurrencesN(linkDisplayW.getSize()));

		totalSize += linkDisplayW.getSize();
		
		
		int size = seeAlsoBoard.load(topic);		
		updateTitle(seeAlsoBoard, Manager.myConstants.seeAlsosN(size));
		
		totalSize += size;
		
		
		manager.getTopicCache().getLinksTo(topic,new StdAsyncCallback("GetLinksTo"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				List list = (List) result;
				
				refPanel.clear();
				
				if(list.size() == 0){
					refPanel.add(new Label(Manager.myConstants.references_none()));					
				}
				
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TopicIdentifier topicIdent = (TopicIdentifier) iter.next();
					refPanel.add(new TopicLink(topicIdent));
				}
				
				updateTitle(refPanel,Manager.myConstants.referencesN(list.size()));
				
				bar.updateTitle(AllReferencesPanel.this,Manager.myConstants.all_referencesN(totalSize + list.size()));
			}
			
		});
		
	}

	private void updateTitle(Widget w, String string) {
		mainPanel.setStackText(mainPanel.getWidgetIndex(w),string);
	}
}
