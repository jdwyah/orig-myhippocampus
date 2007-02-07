package com.aavu.client.gui.glossary;

import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SimpleTopicDisplay extends Composite {

	private VerticalPanel mainP;
	
	public SimpleTopicDisplay(Topic topic) {
		mainP = new VerticalPanel();
		
		
		doEntry(topic);
		doTags(topic);
		doLinks(topic);
				
		initWidget(mainP);
	}
	
	private void doEntry(Topic topic) {
		Entry e = topic.getLatestEntry();
		mainP.add(new HeaderLabel(Manager.myConstants.entry()));
		if(e.isEmpty()){
			mainP.add(new Label(Manager.myConstants.none()));
		}else{
			mainP.add(new TextDisplay(topic.getLatestEntry().getData()));	
		}
	}

	private void doTags(Topic topic) {
		mainP.add(new HeaderLabel(Manager.myConstants.tags(topic.getTitle())));
		if(topic.getTags().size() > 0){
			for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
				Tag tag = (Tag) iter.next();
				showTag(tag);						
			}		
		}else{
			mainP.add(new Label(Manager.myConstants.none()));
		}
	}

	private void showTag(final Tag tag){
		
		TopicLink tagLink = new TopicLink(tag);				
		mainP.add(tagLink);	
		
		displayMetas(tag);
		
	}
	private void displayMetas(Tag tag) {
		Set metas = tag.getMetas();		
//		for (Iterator iter = metas.iterator(); iter.hasNext();) {		
//			Meta element = (Meta) iter.next();
//			GWT.log("displayMetas", null);
//		
//			Widget w = element.getEditorWidget(cur_topic,saveNeeded,manager);
//			tagPanel.add(w);
//
//		}
	}
	
	
	private void doLinks(Topic topic){
		mainP.add(new HeaderLabel(Manager.myConstants.occurrences()));
		for (Iterator iter = topic.getWebLinks().iterator(); iter.hasNext();) {
			WebLink link = (WebLink) iter.next();
			mainP.add(new ExternalLink(link));
		}
	}
	
}
