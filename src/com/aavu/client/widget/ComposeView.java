package com.aavu.client.widget;


import java.util.Iterator;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.TopicServiceAsync;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ComposeView extends Composite implements ClickListener{
	
	private Label titleLabel = new Label("Title: ");
	private TextBox titleBox = new TextBox();
	private TextArea textArea = new TextArea();
	private Button previewButton = new Button("Preview");
	private FlowPanel topicTitlePanel = new FlowPanel();
	
	private Button editTextButton = new Button("Edit Text");
	private Button saveButton = new Button("Save");
	private HTML titleEcho = new HTML();
	private FlowPanel textPanel = new FlowPanel();
	private FlowPanel buttonPanel = new FlowPanel();
	
	private VerticalPanel seeAlsoPanel = new VerticalPanel();
	
	
	private Label serverR = new Label();
	
	private VerticalPanel panel = new VerticalPanel();
	
	public Topic topic;
	private TopicServiceAsync topicServiceA;
	
	
	
	public ComposeView(){
		panel.setSpacing(4);
		titleLabel.addStyleName("ta-compose-Label");
		titleBox.setWidth("30em");
		textArea.setCharacterWidth(50);
		textArea.setHeight("30em");
		
		topicTitlePanel.add(titleLabel);
		topicTitlePanel.add(titleBox);
		
		previewButton.addClickListener(this);
		editTextButton.addClickListener(this);
		saveButton.addClickListener(this);
		
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		panel.setWidth("100%");
		panel.add(topicTitlePanel);
		panel.add(textArea);
		panel.add(previewButton);
		panel.setCellWidth(titleBox,"100%");
		panel.setCellWidth(textArea, "100%");
		
		//for preview
		buttonPanel.add(editTextButton);
		buttonPanel.add(saveButton);
		
		panel.add(serverR);
		setWidget(panel);
	}	
	
	public void load(Topic topic){
		this.topic = topic;
		setupTopic();
		activateEditView();

		Effect.highlight(panel);
	}
		
	private void setupTopic() {
		if(topic != null){
			titleBox.setText(topic.getTitle());
			textArea.setText(topic.getText());
			
			seeAlsoPanel.clear();
			
			if(topic.getSeeAlso() != null){
				Iterator i = topic.getSeeAlso().iterator();
				while (i.hasNext()) {
					Topic also = (Topic) i.next();
					seeAlsoPanel.add(new SeeAlsoComplete(also,topicServiceA));	
				}
			}
			
			seeAlsoPanel.add(new SeeAlsoComplete(new Topic(),topicServiceA));
			
		}
	}

	public void onClick(Widget source){
		if (source == previewButton){
			activateLinkView();
		}
		else if (source == editTextButton){
			activateEditView();
		}
		else if (source == saveButton){
			
			topic.setText(textArea.getText());
			topic.setTitle(titleBox.getText());
			
			topicServiceA.save(topic,new AsyncCallback() {
				public void onFailure(Throwable caught) {					
					serverR.setText("Error "+caught);	    	            
				}

				public void onSuccess(Object result) {
					serverR.setText("Saved");
				}
			});	
		}
	}
	
	public void activateLinkView(){
		
		textPanel.clear();
		textPanel.add(new HTML(textArea.getText()));		
		titleEcho.setHTML(titleBox.getText());
		
		topicTitlePanel.remove(titleBox);
		topicTitlePanel.add(titleEcho);
		
		panel.remove(textArea);
		panel.remove(previewButton);
		
		panel.add(textPanel);
		panel.add(buttonPanel);
	}
	
	public void activateEditView() {
		
		topicTitlePanel.remove(titleEcho);
		topicTitlePanel.add(titleBox);
		
		panel.remove(textPanel);
		panel.remove(buttonPanel);
		
		panel.add(textArea);
		panel.add(previewButton);
		panel.add(seeAlsoPanel);
	}

	public void setTopicServiceA(TopicServiceAsync topicServiceA) {
		this.topicServiceA = topicServiceA;
	}
	
}
