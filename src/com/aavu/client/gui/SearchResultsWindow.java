package com.aavu.client.gui;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.SearchResult;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
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
		super(manager.myConstants.searchResultsTitle());

		mainPanel = new VerticalPanel();
		
		VerticalPanel previewPanel = new VerticalPanel();
				
		if(searchRes != null){
			for (Iterator iter = searchRes.iterator(); iter.hasNext();) {
				SearchResult result = (SearchResult) iter.next();
				mainPanel.add(new SearchLink(manager,result));
			}
			
		}		
		
		setContentPanel(mainPanel);
	}


	private class SearchLink extends Composite {


		public SearchLink(final Manager manager, final SearchResult result) {
			VerticalPanel mainPanel = new VerticalPanel();
			
			Label l = new Label(result.getTitle());
			
			l.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					manager.bringUpChart(result.getTopicIdentifier());
				}				
			});
			
			mainPanel.add(l);			
			mainPanel.add(new HTML(result.getText()));
			mainPanel.add(new Label(manager.myConstants.searchScore()+result.getScore()));
			
			initWidget(mainPanel);
		}
	}
	
}
