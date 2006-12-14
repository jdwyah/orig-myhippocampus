package com.aavu.client.gui;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IslandDetailsWindow extends TopicTagSuperWindow {



	private Tag tag;
	private Manager manager;
	private TopicIdentifier[] topics;


	public IslandDetailsWindow(final Tag tag, TopicIdentifier[] topics,final Manager manager) {
		
		super(manager.newFrame(),manager.myConstants.tagContentsTitle(tag.getTitle()));
				
		this.tag = tag;
		this.manager = manager;		
		this.topics = topics;
		
		init(tag,manager);

	}

	

	//@Override
	protected void addLeftTopExtras(CellPanel panel) {
		//TODO where should this image x/y really be?
		ImageButton addNewButton = new ImageButton(manager.myConstants.topic_new_image(),Dashboard.NEW_BUTTON_W/2,Dashboard.NEW_BUTTON_H/2);		
		addNewButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				Topic t = new Topic(tag.getUser(),manager.myConstants.topic_new_title());
				t.tagTopic(tag);
				manager.bringUpChart(t);
			}});
		addNewButton.addMouseListener(new TooltipListener(manager.myConstants.topic_new_on_island()));		
		
		panel.add(addNewButton);
	}
	
	/**
	 * LeftSide 
	 */
	//@Override
	protected void addLeftExtras(CellPanel panel) {
			
		VerticalPanel leftSideExtra = new VerticalPanel();		
		leftSideExtra.setStyleName("H-IslandTopicList");
		
		leftSideExtra.add(new HeaderLabel(manager.myConstants.island_topics_on()));
		
		PopupPreview previewPop = new PopupPreview();
		
		if(topics != null){
			for (int i = 0; i < topics.length; i++) {
				TopicIdentifier topic = topics[i];
				
				leftSideExtra.add(new TopicPreviewLink(manager,topic,previewPop));
			}
		}
		panel.add(leftSideExtra);
	}
	
	//@Override
	protected void addRightExtras(CellPanel panel) {
		VerticalPanel rightSideExtras = new VerticalPanel();
		rightSideExtras.setStyleName("H-IslandDetailProperties");
		rightSideExtras.add(new HeaderLabel(Manager.myConstants.island_property(),Manager.myConstants.island_property_help()));


		rightSideExtras.add(new TagPropertyPanel(manager, tag));
		panel.add(rightSideExtras);
	}


	private class TopicPreviewLink extends Composite {

		public TopicPreviewLink(final Manager manager, final TopicIdentifier ident, final PopupPreview preview) {
			HorizontalPanel mainPanel = new HorizontalPanel();
			
			Label l = new Label(ident.getTopicTitle());
//			l.addMouseListener(new MouseListenerAdapter(){
//				  public void onMouseEnter(Widget sender) {
//					  manager.getTopicCache().getTopic(ident, new StdAsyncCallback("Preview"){
//						public void onSuccess(Object result) {
//							super.onSuccess(result);
//
//							preview.setPopupPosition(getAbsoluteLeft()+30, getAbsoluteTop()+30);
//							preview.setTopic((Topic)result);
//							preview.show();
//							
//						}});
//				  }});
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
