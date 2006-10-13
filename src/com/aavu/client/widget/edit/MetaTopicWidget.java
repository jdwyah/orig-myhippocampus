package com.aavu.client.widget.edit;

import java.util.Iterator;
import java.util.Map.Entry;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.widget.tags.SaveListener;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopicWidget extends SaveListener implements CompleteListener {

	private Topic topic;
	private MetaTopic meta;
	private TopicCompleter completer; 

	public MetaTopicWidget(final MetaTopic meta, final Topic topic) {
		
		HorizontalPanel widget = new HorizontalPanel();

		this.topic = topic;
		this.meta = meta;
						
		completer = new TopicCompleter();
		completer.addListener(this);
		
		Topic mv = (Topic) topic.getSingleMetaValueFor(meta);
			
		if(mv != null){
			completer.setText(mv.getTitle());		    	
		}
	
		
		widget.add(new Label(meta.getName()));
		widget.add(completer);

		initWidget(widget);
	}

	//@Override
	/**
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
	

	public void completed(final String completeText) {
		System.out.println("COMPLETED LISTENER!");
		completer.getTopicIdentForNameOrCreateNew(completeText,new StdAsyncCallback("SeeAlsoComplete"){
			public void onSuccess(Object result) {
				System.out.println("SUCCESS "+result);
				TopicIdentifier to = (TopicIdentifier) result;
				System.out.println("ss "+to);
				
				topic.addMetaValue(meta, new Topic(to));
				
				completer.setText(completeText);

			}});
	}
}
