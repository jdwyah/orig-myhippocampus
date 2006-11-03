package com.aavu.client.widget;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ReferencePanel extends Composite {

	private Topic topic;
	private Manager manager;
	private FlowPanel refPanel;
	
	public ReferencePanel(Manager manager,Topic t){
		this.topic = t;
		this.manager = manager;
	
		refPanel = new FlowPanel();
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(new HeaderLabel(Manager.myConstants.references()));
		mainPanel.add(refPanel);
		initWidget(mainPanel);
	}

	public void load() {
		manager.getTopicCache().getLinksTo(topic,new StdAsyncCallback("GetLinksTo"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				List list = (List) result;
				
				refPanel.clear();
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TopicIdentifier topicIdent = (TopicIdentifier) iter.next();
					refPanel.add(new TopicLink(topicIdent));
				}
				
			}
			
		});
		
	}
}
