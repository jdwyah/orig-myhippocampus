package com.aavu.client.gui.glossary;

import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SimpleTopicDisplay extends Composite {

	private VerticalPanel mainP;
	
	/**
	 * Pass a manager to enable the "Go There now" link.
	 * @param topic
	 */
	public SimpleTopicDisplay(Topic topic) {
		this(topic,null,null);
	}
	
	/**
	 * This CTOR enables to "Go There Now" link.
	 * @param topic
	 * @param manager
	 */
	public SimpleTopicDisplay(final Topic topic, final Manager manager, final CloseListener close) {
		mainP = new VerticalPanel();		
		
		doEntry(topic);
		doTags(topic);
		doLinks(topic);
		
		if(manager != null){	
			Button goThere = new Button(ConstHolder.myConstants.topic_preview_gotherenow());
			goThere.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					manager.bringUpChart(topic.getId());
					close.close();					
				}});
			mainP.add(goThere);
		}
				
		initWidget(mainP);
	}

	private void doEntry(Topic topic) {
		Entry e = topic.getLatestEntry();
		mainP.add(new HeaderLabel(ConstHolder.myConstants.entry()));
		if(e.isEmpty()){
			mainP.add(new Label(ConstHolder.myConstants.none()));
		}else{
			mainP.add(new TextDisplay(topic.getLatestEntry().getData()));	
		}
	}

	private void doTags(Topic topic) {
		mainP.add(new HeaderLabel(ConstHolder.myConstants.tags()));
		if(topic.getTags().size() > 0){
			for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
				Tag tag = (Tag) iter.next();
				showTag(tag);						
			}		
		}else{
			mainP.add(new Label(ConstHolder.myConstants.none()));
		}
	}

	private void showTag(final Tag tag){
		
		TopicLink tagLink = new TopicLink(tag);				
		mainP.add(tagLink);	
		
		displayMetas(tag);
		
	}
	private void displayMetas(final Tag tag) {
		Set metas = tag.getTagProperties();		
//		for (Iterator iter = metas.iterator(); iter.hasNext();) {		
//			Meta element = (Meta) iter.next();
//			GWT.log("displayMetas", null);
//		
//			Widget w = element.getEditorWidget(cur_topic,saveNeeded,manager);
//			tagPanel.add(w);
//
//		}
	}


	private void doLinks(final Topic topic){
		mainP.add(new HeaderLabel(ConstHolder.myConstants.occurrences()));
		Set weblinks = topic.getWebLinks();
		if(!weblinks.isEmpty()){
			for (Iterator iter = topic.getWebLinks().iterator(); iter.hasNext();) {
				WebLink link = (WebLink) iter.next();
				mainP.add(new ExternalLink(link));
			}
		}
		else {
			mainP.add(new Label(ConstHolder.myConstants.none()));
		}
	}
	
}
