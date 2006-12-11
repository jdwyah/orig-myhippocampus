package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.TopicWidget;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IslandDetailsWindow extends PopupWindow {

	private HorizontalPanel mainPanel;	
	private PopupPreview preview;

	public IslandDetailsWindow(String title, TopicIdentifier[] topics,Manager manager) {
		super(manager.newFrame(),manager.myConstants.tagContentsTitle());

		mainPanel = new HorizontalPanel();
		
		
		HorizontalPanel buttons = new HorizontalPanel();		
		buttons.add(new Button("Add to this Island"));
		
		VerticalPanel leftSide = new VerticalPanel();		
		leftSide.add(buttons);		
		
		
		PopupPreview previewPop = new PopupPreview();
		
		if(topics != null){
			for (int i = 0; i < topics.length; i++) {
				TopicIdentifier topic = topics[i];
				leftSide.add(new TopicPreviewLink(manager,topic,previewPop));
			}
		}
		
		
		
		VerticalPanel rightSide = new VerticalPanel();
		rightSide.add(new Label("Properties (edit)"));
		rightSide.add(new Label("Date Read"));
		rightSide.add(new Label("Rating"));
		rightSide.add(new Label("Genre"));
		
		mainPanel.add(leftSide);
		mainPanel.add(rightSide);
		
		setContent(mainPanel);

	}


	private class TopicPreviewLink extends Composite {

		public TopicPreviewLink(final Manager manager, final TopicIdentifier ident, final PopupPreview preview) {
			HorizontalPanel mainPanel = new HorizontalPanel();
			
			Label l = new Label(ident.getTopicTitle());
			l.addMouseListener(new MouseListenerAdapter(){
				  public void onMouseEnter(Widget sender) {
					  manager.getTopicCache().getTopic(ident, new StdAsyncCallback("Preview"){
						public void onSuccess(Object result) {
							super.onSuccess(result);

							preview.setPopupPosition(getAbsoluteLeft()+30, getAbsoluteTop()+30);
							preview.setTopic((Topic)result);
							preview.show();
							
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


	private static class PopupPreview extends PopupPanel {

		public PopupPreview() {
			super(true);		
			setStyleName("H-PopupPreview");
		}

		public void setTopic(Topic topic) {
			setWidget(new TextDisplay(topic.getLatestEntry().getData()));			
		}
	}



	
}
