package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ActionableTopicLabel extends Composite {

	private Label actionText;
	private TopicLink theLabel; 
	
	public ActionableTopicLabel(String string, ClickListener action) {
		this(null,string,action);
	}

	public ActionableTopicLabel(Topic topic, String actionTextStr) {
		this(topic,actionTextStr,Orientation.HORIZONTAL);
	}
	public ActionableTopicLabel(Topic topic, String actionTextStr,Orientation orient) {
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
		actionText.setVisible(false);
		
		CellPanel hp;
		if(orient == Orientation.HORIZONTAL){
			hp = new HorizontalPanel();
		}
		else{
			hp = new VerticalPanel();
		}
		
		hp.add(theLabel);
		hp.add(actionText);
		
		fp.add(hp);
		initWidget(fp);
		
		addStyleName("H-TopicLink");
	}
	public ActionableTopicLabel(Topic topic,String actionTextStr,ClickListener action) {
		this(topic,actionTextStr);
		actionText.addClickListener(action);
	}
	
	public void addActionListener(ClickListener action){
		actionText.addClickListener(action);
	}
	
	public void setTopicIdent(TopicIdentifier to) {
		theLabel.load(to);		
	}

	

}
