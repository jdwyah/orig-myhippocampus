package com.aavu.client.gui;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.SearchResult;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.util.DecimalFormatSimple;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchResultsWindow extends PopupWindow {

	private VerticalPanel mainPanel;	

	private final DecimalFormatSimple format = new DecimalFormatSimple(2);
	
	public SearchResultsWindow(Manager manager, List searchRes) {
		super(manager.newFrame(),manager.myConstants.searchResultsTitle());

		mainPanel = new VerticalPanel();
		
		
		
		
		if(searchRes == null || searchRes.size() == 0){
			mainPanel.add(new Label(manager.myConstants.searchResultsNone()));
		}
		else{
			mainPanel.add(new Label(Manager.myConstants.searchResultsN(searchRes.size())));
			
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
			
						
			TopicLink tl = new TopicLink(result.getTopicIdentifier(),SearchResultsWindow.this);
			tl.addStyleName("H-Title");
			
			mainPanel.addStyleName("H-SearchResults");
			
			
			Label scoreL = new Label("Score: "+format.format(result.getScore()));
			scoreL.setStyleName("H-Score");
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(tl);
			hp.add(scoreL);
			
			if(result.getText() == null){
				mainPanel.add(hp);							
				//mainPanel.add(new Label(manager.myConstants.searchScore()+result.getScore()));				
			}else{
				mainPanel.add(hp);			
				HTML h = new HTML(result.getText());
				h.setStyleName("H-Text");
				mainPanel.add(h);
				//mainPanel.add(new Label(manager.myConstants.searchScore()+result.getScore()));				
			}			
			
			initWidget(mainPanel);
		}
	}
	
}
