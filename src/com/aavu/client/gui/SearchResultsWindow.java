package com.aavu.client.gui;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.SearchResult;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.TopicWidget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SearchResultsWindow extends PopupWindow {

	private VerticalPanel mainPanel;	


	public SearchResultsWindow(Manager manager, List searchRes) {
		super(manager.newFrame(),manager.myConstants.searchResultsTitle());

		mainPanel = new VerticalPanel();
		
		VerticalPanel previewPanel = new VerticalPanel();
				
		
		if(searchRes == null || searchRes.size() == 0){
			mainPanel.add(new Label(manager.myConstants.searchResultsNone()));
		}
		else{
			for (Iterator iter = searchRes.iterator(); iter.hasNext();) {
				SearchResult result = (SearchResult) iter.next();
				mainPanel.add(new SearchLink(manager,result));
			}			
		}		
		
		setContent(mainPanel);
	}


	private class SearchLink extends Composite {


		public SearchLink(final Manager manager, final SearchResult result) {
			VerticalPanel mainPanel = new VerticalPanel();
			
			TopicLink tl = new TopicLink(result.getTopicIdentifier());
			tl.addStyleName("H-Title");
			mainPanel.addStyleName("H-SearchResults");
			
			if(result.getText() == null){
				mainPanel.add(tl);							
				//mainPanel.add(new Label(manager.myConstants.searchScore()+result.getScore()));				
			}else{
				mainPanel.add(tl);			
				mainPanel.add(new HTML(result.getText()));
				//mainPanel.add(new Label(manager.myConstants.searchScore()+result.getScore()));				
			}			
			
			initWidget(mainPanel);
		}
	}
	
}
