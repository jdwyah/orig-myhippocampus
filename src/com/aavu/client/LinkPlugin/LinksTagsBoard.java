package com.aavu.client.LinkPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.util.Logger;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.DeletableTopicLabel;
import com.aavu.client.widget.edit.RemoveListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinksTagsBoard extends Composite implements RemoveListener {

	private VerticalPanel mainPanel;
	private Map topicM;
	private HorizontalPanel onPanel;
	private TopicCompleter topicCompleter;
	
	public LinksTagsBoard(final CompleteListener completer,TopicCache topicCache){
		
		onPanel = new HorizontalPanel();	
		mainPanel = new VerticalPanel();
		
		topicCompleter = new TopicCompleter(topicCache);		
		topicCompleter.addListener(completer);
		

		EnterInfoButton enterInfoButton = new EnterInfoButton();		
		enterInfoButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				completer.completed(topicCompleter.getText());
			}
		});
				
		
		mainPanel.add(onPanel);
		
		HorizontalPanel tp = new HorizontalPanel();
		tp.add(topicCompleter);
		tp.add(enterInfoButton);
		mainPanel.add(tp);		
		
		initWidget(mainPanel);
	}

	public void load(WebLink w) {
		
		topicM = new HashMap();
		
		if(w != null){
			Logger.log("found "+w.getTopics().size()+" Tags for this link");
			for (Iterator iter = w.getTopics().iterator(); iter.hasNext();) {
				Topic t = (Topic) iter.next();
				
				DeletableTopicLabel tagLabel = new DeletableTopicLabel(t,this);
				
				add(t.getIdentifier(),tagLabel);
				
			}		
		}
	}


	public void remove(Topic topic, Widget widgetToRemoveOnSuccess) {
		Object o = topicM.get(topic);
		
		if(o != null){
			DeletableTopicLabel label = (DeletableTopicLabel) o;
			onPanel.remove(label);
			topicM.remove(topic);
		}
	}

	public void add(TopicIdentifier to,Widget w) {
		
		topicM.put(to, w);
		
		onPanel.add(w);
	}

	public Topic getFirst() {
		if(topicM.size() < 1){
			return null;
		}
		return (Topic) ((Entry)topicM.entrySet().iterator().next()).getValue();
	}
	
	
}
