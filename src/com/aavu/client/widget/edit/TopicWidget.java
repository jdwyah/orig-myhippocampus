package com.aavu.client.widget.edit;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.util.SimpleDateFormatGWT;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicWidget extends Composite {
	
	
	private FlowPanel topicTitlePanel = new FlowPanel();
	private Label titleLabel = new Label("Title: ");
		
	private FlowPanel textPanel = new FlowPanel();
	
	private VerticalPanel panel = new VerticalPanel();
	
	public Topic topic;
	private GWTTopicServiceAsync topicServiceA;
	private TopicList topicList;
	
	private static SimpleDateFormatGWT df; 
	
	
	public TopicWidget(Topic topic){
				
		panel.setSpacing(4);		
		titleLabel.addStyleName("ta-compose-Label");
		
		topicTitlePanel.add(titleLabel);
		topicTitlePanel.addStyleName("middle-column-box-title-blue");
		
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		panel.setWidth("100%");
		panel.add(topicTitlePanel);

		load(topic);
		
		initWidget(panel);
	}	
	
	public void load(Topic topic){
		this.topic = topic;
		
		setup();

		Effect.highlight(panel);
	}
		
	public void setup(){
		
		textPanel.clear();
		textPanel.add(new TextDisplay(topic.getText()));		
		
		topicTitlePanel.clear();
		topicTitlePanel.add(titleLabel);
		topicTitlePanel.add(new Label(topic.getTitle()));
		topicTitlePanel.add(new Label(" Updated: "+formatDate(topic.getLastUpdated())));

				
		panel.clear();
		panel.add(topicTitlePanel);

		
		panel.add(doTags(topic));
		
		panel.add(textPanel);
		
	}
	
	private Widget doTags(Topic t){
		VerticalPanel panel = new VerticalPanel();	
		for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
			Tag element = (Tag) iter.next();
			panel.add(new Label(element.getName()));
			displayMetas(element,t.getMetaValueStrs(),panel);
		}		
		return panel;
	}
	
	private void displayMetas(Tag tag,Map mmap,VerticalPanel tagPanel) {
		List metas = tag.getMetas();
		for (Iterator iter = metas.iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			tagPanel.add(element.getWidget(mmap));
		}
	}

	public void setTopicServiceA(GWTTopicServiceAsync topicServiceA) {
		this.topicServiceA = topicServiceA;
	}
	
	
	public String formatDate(java.util.Date date){		
		if(df == null){
			df = new SimpleDateFormatGWT("yyyy-MM-dd HH:mm:ss");
		}
		if(date != null){
			return df.format(date);
		}
		return null;
	}
	
}
