package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.TopicWidget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicDisplayWindow extends PopupWindow {

	private VerticalPanel mainPanel;	

	public TopicDisplayWindow(String title, TopicIdentifier[] topics,Manager manager) {
		super(manager.myConstants.tagContentsTitle());

		mainPanel = new VerticalPanel();
		
		VerticalPanel previewPanel = new VerticalPanel();
				
		if(topics != null){
			for (int i = 0; i < topics.length; i++) {
				TopicIdentifier topic = topics[i];
				mainPanel.add(new TopicPreviewLink(manager,topic,previewPanel));
			}
		}
		mainPanel.add(previewPanel);
		
		setContentPanel(mainPanel);

	}


	private class TopicPreviewLink extends Composite {

		public TopicPreviewLink(final Manager manager, final TopicIdentifier ident,final Panel previewWindow) {
			HorizontalPanel mainPanel = new HorizontalPanel();
			
			Label l = new Label(ident.getTopicTitle());
			l.addMouseListener(new MouseListenerAdapter(){
				  public void onMouseEnter(Widget sender) {
					  manager.getTopicCache().getTopic(ident, new StdAsyncCallback("Preview"){
						public void onSuccess(Object result) {
							super.onSuccess(result);
							previewWindow.clear();
							previewWindow.add(new TopicWidget(manager,(Topic)result));
						}});
				  }});
			l.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					manager.bringUpChart(ident);
				}				
			});
			
			mainPanel.add(l);
			
			initWidget(mainPanel);
		}
	}
	
}
