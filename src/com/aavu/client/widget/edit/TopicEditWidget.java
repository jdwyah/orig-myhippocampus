package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.widget.RichText2.HippoEditor;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicEditWidget extends Composite {

	private TextBox titleBox = new TextBox();
	private HippoEditor textArea = new HippoEditor();
	

	private TagBoard tagBoard;
	
	private Topic topic;
	private TopicViewAndEditWidget topicViewAndEditWidget;
	
	
	
	public TopicEditWidget(TopicViewAndEditWidget topicViewAndEditWidget, HippoCache hippoCache, Topic topic){
		this.topic = topic;
		this.topicViewAndEditWidget = topicViewAndEditWidget;
		
		System.out.println("topic edit widg "+topic);
				
		tagBoard = new TagBoard(hippoCache);
		
		setupTopic();
		
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(new Label("Title"));
		panel.add(titleBox);
		
		panel.add(tagBoard);		
		
		//textArea.set
		
//		SpecialTextbox	ww = new SpecialTextbox();
//		FocusPanel fp = new FocusPanel();
//		fp.addKeyboardListener(ww);
//		
//		fp.add(ww);
//		panel.add(fp);
		
		panel.add(textArea);
	
	
		initWidget(panel);
	}
	
	
	
	private void setupTopic() {
		System.out.println("setupTopic");
		if(topic != null){
			titleBox.setText(topic.getTitle());		
			textArea.setText(topic.getText());
						
			tagBoard.load(topic);
		}
	}



	public void save() {
		topic.setText(textArea.getText());
		topic.setTitle(titleBox.getText());
		
		tagBoard.saveThingsNowEvent(new StdAsyncCallback("save things now"){

			public void onSuccess(Object result) {
				topicViewAndEditWidget.save(topic);					
			}});
	}

}
