package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

public class ActionableTopicLabel extends Composite {

	private Label actionText;
	private TopicLink theLabel; 
	
	public ActionableTopicLabel(String string, ClickListener action) {
		this(null,string,action);
	}
	public ActionableTopicLabel(Topic topic,String actionTextStr,ClickListener action) {
		FocusPanel fp = new FocusPanel();
		
					
		fp.addMouseListener(new MouseListenerAdapter(){

			public void onMouseEnter(Widget sender) {
				actionText.setVisible(true);
			}

			public void onMouseLeave(Widget sender) {
				actionText.setVisible(false);
			}

		});
		
		if(topic != null){
			theLabel = new TopicLink(topic);	
		}else{
			theLabel = new TopicLink();
		}
		actionText = new Label(actionTextStr);		
		actionText.addClickListener(action);
		actionText.setVisible(false);
		
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.add(theLabel);
		hp.add(actionText);
		
		fp.add(hp);
		initWidget(fp);
	}
	
	public void setTopicIdent(TopicIdentifier to) {
		theLabel.load(to);		
	}

	

}
