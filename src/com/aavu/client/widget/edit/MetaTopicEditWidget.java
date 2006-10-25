package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.tags.SaveListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopicEditWidget extends SaveListener implements CompleteListener {

	private Topic topic;
	private MetaTopic meta;
	private TopicCompleter completer; 
	private ActionableTopicLabel topicDisplayLink;
	private EnterInfoButton enterB;
	
	public MetaTopicEditWidget(final MetaTopic meta, final Topic topic) {
		
		HorizontalPanel widget = new HorizontalPanel();

		this.topic = topic;
		this.meta = meta;
						
		completer = new TopicCompleter();
		completer.addListener(this);
		
	
		enterB = new EnterInfoButton();
		enterB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				completed(completer.getText());	
			}});
		
		topicDisplayLink = new ActionableTopicLabel(Manager.myConstants.editMe(),new ClickListener(){
			public void onClick(Widget sender) {
				setToEditMode();
			}});
						
		widget.add(new Label(meta.getName()));
		widget.add(completer);
		widget.add(topicDisplayLink);
		widget.add(enterB);
		
		Topic mv = (Topic) topic.getSingleMetaValueFor(meta);		
		if(mv != null){
			completer.setText(mv.getTitle());	
			setToShowMode(mv.getIdentifier());
		}
		
		initWidget(widget);
	}

	private void setToEditMode(){
		completer.setVisible(true);
		topicDisplayLink.setVisible(false);
		enterB.setVisible(true);		
	}
	private void setToShowMode(TopicIdentifier to){

		topicDisplayLink.setTopicIdent(to);

		topicDisplayLink.setVisible(true);				
		completer.setVisible(false);
		enterB.setVisible(false);	
	}
	
	//@Override
	/**
	 * NOTE not used! replaced w/ completed
	 * do a local lookup for Topic, if not found, create a new one.
	 */
	public void saveNowEvent() {
//		
//		Topic completed = completer.getTopicCompletedOrNullForNew();
//		System.out.println("Completed "+completed);
//		if(completed == null){
//			completed = new Topic(topic.getUser(),completer.getText());			
//		}		
//		
//		topic.addMetaValue(meta, completed);
	}
	

	/**
	 * replaces the saveNowEvent
	 * 
	 * TODO Probably a race condition if they meta value-> then hit save before this returns.
	 * 
	 */
	public void completed(final String completeText) {
		System.out.println("COMPLETED LISTENER!");
		completer.getTopicIdentForNameOrCreateNew(completeText,new StdAsyncCallback("SeeAlsoComplete"){
			public void onSuccess(Object result) {
				System.out.println("GetTopicIdentForNameOrCreateNew: "+result);
				TopicIdentifier to = (TopicIdentifier) result;
				System.out.println("GetTopicIdentForNameOrCreateNewID: "+to);
				
				topic.addMetaValue(meta, new Topic(to));
				
				setToShowMode(to);
				
			}});
	}
}
