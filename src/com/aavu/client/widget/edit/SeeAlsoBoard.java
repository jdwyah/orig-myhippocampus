package com.aavu.client.widget.edit;

import java.util.Iterator;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.SeeAlso;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SeeAlsoBoard extends Composite implements CompleteListener {

	private TopicCompleter topicCompleter;
	private TopicCache topicService;
	private Topic myTopic;
	
	private SeeAlsoWidget alsos;
	
	public SeeAlsoBoard(Manager manager) {
		topicService = manager.getTopicCache();
		
		topicCompleter = new TopicCompleter();		
		topicCompleter.addListener(this);

		alsos = new SeeAlsoWidget();
		
		
		VerticalPanel mainP = new VerticalPanel();
		
		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new HeaderLabel(Manager.myConstants.seeAlsos()));
		cp.add(topicCompleter);
		
		mainP.add(cp);
		mainP.add(alsos);
		
		
		initWidget(mainP);
	}

	public void load(Topic topic) {
		myTopic = topic;
		
		
		SeeAlso assoc = myTopic.getSeeAlsos();
		if(assoc == null){
			System.out.println("no see alsos");
		}else{
			alsos.load(assoc);
		}
		
	}

	public void completed(String completeText) {
		
		topicService.getTopicIdentForNameOrCreateNew(completeText,new StdAsyncCallback("SeeAlsoComplete"){
			public void onSuccess(Object result) {
				TopicIdentifier to = (TopicIdentifier) result;
				
//				myTopic.addSeeAlso(o);
				
				SeeAlso seeAlso = myTopic.getSeeAlsos();
				if(myTopic.getSeeAlsos() == null){
					seeAlso = new SeeAlso();
					seeAlso.add(myTopic.getIdentifier());
				}
				
				seeAlso.add(to);
				
				alsos.add(to);
					
			}});
		
	}
	
	/**
	 * display all the see also's as links to their topic.
	 * 
	 * @author Jeff Dwyer
	 */
	private class SeeAlsoWidget extends Composite {

		private HorizontalPanel horizP;
		
		public SeeAlsoWidget(){			
			horizP = new HorizontalPanel();			
			initWidget(horizP);
		}
		
		public void load(SeeAlso seeAlso){
			for (Iterator iter = seeAlso.getMembers().values().iterator(); iter.hasNext();) {
				Topic top = (Topic) iter.next();
				horizP.add(new TopicLink(top));
			}
		}

		public void add(TopicIdentifier to2) {
			horizP.add(new TopicLink(to2));			
		}		
	}

}
