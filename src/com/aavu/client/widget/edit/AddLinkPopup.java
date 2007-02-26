package com.aavu.client.widget.edit;

import java.util.Iterator;

import org.gwm.client.GInternalFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.SaveOccurrenceCommand;
import com.aavu.client.domain.commands.SaveSeeAlsoCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.LinkDisplayWidget;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddLinkPopup extends PopupWindow implements CompleteListener {

	private static final int HEIGHT = 200;
	private static final int WIDTH = 550;
	private WebLink link;
	private Label descRequired;
	private Label linkRequired;
	private TopicCompleter topicCompleter;
	private Manager manager;
	private HorizontalPanel onPanel;
	private TextArea notesT;
	private TextBox urlT;
	private TextBox descriptionT;

	public AddLinkPopup(final LinkDisplayWidget widget, Manager _manager, GInternalFrame frame, WebLink _link, final Topic myTopic) {
		super(frame, Manager.myConstants.link_add_title(),WIDTH,HEIGHT);
		this.manager = _manager;
		this.link = _link;
		
		Grid mainPanel = new Grid(5,3);
		
		mainPanel.setWidget(0, 0, new Label(Manager.myConstants.link_description()));		
		mainPanel.setWidget(1, 0, new Label(Manager.myConstants.link_url()));
		mainPanel.setWidget(2, 0, new Label(Manager.myConstants.link_notes()));
		
		
		/*
		 * Add a panel showing which other topics this link is associated with 
		 */
		mainPanel.setWidget(3, 0, new Label(Manager.myConstants.link_topics()));
				
		mainPanel.setWidget(3, 1, getOnPanel());
				
		
		descRequired = new Label();
		linkRequired = new Label();
		mainPanel.setWidget(0, 2, descRequired);
		mainPanel.setWidget(1, 2, linkRequired);
		
		descriptionT = new TextBox();
		descriptionT.setSize("35em","2em");
		if(link.getDescription() != null){
			descriptionT.setText(link.getDescription());
		}
		urlT = new TextBox();
		urlT.setSize("35em","2em");		
		if(link.getUri() != null){
			urlT.setText(link.getUri());
		}
		
		notesT = new TextArea();
		notesT.setSize("30em", "7em");
		if(link.getNotes() != null){
			notesT.setText(link.getNotes());
		}
		
		mainPanel.setWidget(0, 1, descriptionT);
		mainPanel.setWidget(1, 1, urlT);
		mainPanel.setWidget(2, 1, notesT);
		
		Button submitB = new Button(Manager.myConstants.save());
		submitB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				
				
				
				if(!prepareLink()){
					return;
				}
				
//				if(link.getId() == 0){
//					myTopic.getOccurences().add(link);
//				}
				
				manager.getTopicCache().save(myTopic,new SaveOccurrenceCommand(myTopic, link),						
						new StdAsyncCallback(Manager.myConstants.save()){});
				
				widget.load(myTopic);
				close();
			}});
		
		mainPanel.setWidget(4, 0, submitB);
		
		setContent(mainPanel);
	}

	
	
	private Widget getOnPanel() {
		onPanel = new HorizontalPanel();	
		
		HorizontalPanel rtnPanel = new HorizontalPanel();
		
		topicCompleter = new TopicCompleter(manager.getTopicCache());		
		topicCompleter.addListener(this);

		
		EnterInfoButton enterInfoButton = new EnterInfoButton();		
		enterInfoButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				completed(topicCompleter.getText());
			}
		});
		
		for (Iterator iter = link.getTopics().iterator(); iter.hasNext();) {
			Topic top = (Topic) iter.next();			
			onPanel.add(new TopicLink(top));
		}
		
		rtnPanel.add(onPanel);
		rtnPanel.add(topicCompleter);
		rtnPanel.add(enterInfoButton);
		
		return rtnPanel;		
	}

	public void completed(String completeText) {
		manager.getTopicCache().getTopicIdentForNameOrCreateNew(completeText,new StdAsyncCallback(Manager.myConstants.seeAlso_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TopicIdentifier to = (TopicIdentifier) result;
				
				onPanel.add(new TopicLink(to));
				topicCompleter.setText("");
				
				prepareLink();
				
				manager.getTopicCache().save(new Topic(to),new SaveOccurrenceCommand(new Topic(to), link),						
						new StdAsyncCallback(Manager.myConstants.save()){});								
				
			}});
	}


	/**
	 * Create the link object.
	 * 
	 * 
	 * @param descriptionT
	 * @param urlT
	 * @param notesT
	 * @return false if required fields are not there
	 */
	private boolean prepareLink() {
		if(descriptionT.getText().length() == 0){
			descRequired.setText(Manager.myConstants.required());
			return false;
		}
		if(urlT.getText().length() == 0){
			linkRequired.setText(Manager.myConstants.required());
			return false;
		}
		link.setDescription(descriptionT.getText());
		link.setNotes(notesT.getText());
		link.setUri(urlT.getText());
		
		return true;
	}

	
	
}
