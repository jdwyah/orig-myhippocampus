package com.aavu.client.widget;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.TooltipListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopicLink extends Composite implements ClickListener {

	private static final int DEFAULT_MAX_STRING = 40;
	private Label l;
	private long id; 
	
	/**
	 * dummyLink
	 *
	 */
	public TopicLink(){
		this("",0);
	}
	public TopicLink(final Topic to) {
		this(to.getTitle(),to.getId());
	}

	public TopicLink(TopicIdentifier topic) {
		this(topic.getTopicTitle(), topic.getTopicID());
	}
	private TopicLink(String title, final long id){
		this(title,id,DEFAULT_MAX_STRING);
	}
	public TopicLink(TopicIdentifier topic, int maxStringLength) {
		this(topic.getTopicTitle(),topic.getTopicID(),maxStringLength);
	}

	private TopicLink(String title, long id, int maxStringLength){

		l = null;
		if(title.length() > maxStringLength){
			l = new Label(title.substring(0, maxStringLength-3)+"...");
			l.addMouseListener(new TooltipListener(0,0,title));
		}else{
			l = new Label(title);
		}
		this.id = id;
		l.addClickListener(this);


		l.setStyleName("H-TopicLink");

		initWidget(l);
	}
	public void load(TopicIdentifier to) {
		l.setText(to.getTopicTitle());		
		id = to.getTopicID();
	}
	public void onClick(Widget sender) {		
		History.newItem(id+"");		
	}
	

}
