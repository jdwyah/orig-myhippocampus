package com.aavu.client.LinkPlugin;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.SaveOccurrenceCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.gadgets.TopicLoader;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddLinkContent extends Composite implements CompleteListener {

	private WebLink link;
	private Label descRequired;
	private Label linkRequired;
	private TopicCompleter topicCompleter;
	private TopicCache topicCache;

	private TextArea notesT;
	private TextBox urlT;
	private TextBox descriptionT;
	private LinksTagsBoard tagsBoard;
	

	
	//http://localhost:8888/com.aavu.AddLink/AddLink.html?url=http%3A%2F%2Fwww.resourcebundleeditor.com%2Fwiki%2FSupport&description=ResourceBundle%20Editor&notes=pport%20mechanisms%20are%20hosted%20on%20
	//http://localhost:8888/com.aavu.AddLink/AddLink.html?url=http%3A%2F%2Fwww.google.com&description=ResourceBundle%20Editor&notes=pport%20mechanisms%20are%20hosted%20on%20

	
	public AddLinkContent(final TopicLoader widget, TopicCache _topicCache, WebLink _link, final Topic myTopic, final CloseListener closeListener) {		
		this.topicCache = _topicCache;
		this.link = _link;
		
		
		Grid mainPanel = new Grid(5,3);
		
		mainPanel.setWidget(0, 0, new Label(ConstHolder.myConstants.link_description()));		
		mainPanel.setWidget(1, 0, new Label(ConstHolder.myConstants.link_url()));
		mainPanel.setWidget(2, 0, new Label(ConstHolder.myConstants.link_notes()));
		
		
		/*
		 * Add a panel showing which other topics this link is associated with 
		 */
		mainPanel.setWidget(3, 0, new Label(ConstHolder.myConstants.link_topics()));
				
		tagsBoard = new LinksTagsBoard(this,topicCache);
		tagsBoard.load(link);
		mainPanel.setWidget(3, 1, tagsBoard);
				
		
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
		
		Button submitB = new Button(ConstHolder.myConstants.save());
		submitB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				
				
				
				if(!prepareLink()){
					return;
				}
				
//				if(link.getId() == 0){
//					myTopic.getOccurences().add(link);
//				}
				
							
				if(myTopic != null){
					topicCache.save(myTopic,new SaveOccurrenceCommand(myTopic, link),						
							new StdAsyncCallback(ConstHolder.myConstants.save()){});
					widget.load(myTopic);
				} else {
					System.out.println("SAVE WITH NO DEFAULT TOPIC");
					
					Topic first = tagsBoard.getFirst();
					
					if(first == null){
						Window.alert(ConstHolder.myConstants.link_please_pick());
						return;
					}else{
						System.out.println("Should be good to go just save to first");
					}
					
					
				}
				
				
				closeListener.close();
			}});
		
		mainPanel.setWidget(4, 0, submitB);
		
		initWidget(mainPanel);
	}

	
	
	

	public void completed(String completeText) {
		topicCache.getTopicIdentForNameOrCreateNew(completeText,new StdAsyncCallback(ConstHolder.myConstants.seeAlso_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TopicIdentifier to = (TopicIdentifier) result;
								
				tagsBoard.add(to,new TopicLink(to));
				
				topicCompleter.setText("");
				
				prepareLink();
				
				topicCache.save(new Topic(to),new SaveOccurrenceCommand(new Topic(to), link),						
						new StdAsyncCallback(ConstHolder.myConstants.save()){});								
				
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
			descRequired.setText(ConstHolder.myConstants.required());
			return false;
		}
		if(urlT.getText().length() == 0){
			linkRequired.setText(ConstHolder.myConstants.required());
			return false;
		}
		link.setDescription(descriptionT.getText());
		link.setNotes(notesT.getText());
		link.setUri(urlT.getText());
		
		return true;
	}
	
}
