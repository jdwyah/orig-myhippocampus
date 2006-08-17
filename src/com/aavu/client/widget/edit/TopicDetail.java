package com.aavu.client.widget.edit;


import java.util.Iterator;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.TagServiceAsync;
import com.aavu.client.service.remote.TopicServiceAsync;
import com.aavu.client.util.SimpleDateFormatGWT;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicDetail extends Composite implements ClickListener{


	private FlowPanel topicTitlePanel = new FlowPanel();
	private TextBox titleBox = new TextBox();
	private Label titleEcho = new Label();
	private Label titleLabel = new Label("Title: ");

	private TextArea textArea = new TextArea();

	private Button cancelButton = new Button("Cancel");

	private Button editTextButton = new Button("Edit Text");
	private Button saveButton = new Button("Save");

	private FlowPanel textPanel = new FlowPanel();
	private FlowPanel buttonPanel = new FlowPanel();

	private VerticalPanel seeAlsoPanel = new VerticalPanel();

	private FlowPanel seeAlsoStatic = new FlowPanel();
	private SeeAlsoComplete seeAlsoComplete;

	private Label serverR = new Label();

	private VerticalPanel detailPanel = new VerticalPanel();
	private HorizontalPanel mainPanel = new HorizontalPanel();

	public Topic topic;
	private TopicServiceAsync topicServiceA;
	private TagServiceAsync tagServiceA;
	private TopicList topicList;

	private TagBoard tagBoard;

	private static SimpleDateFormatGWT df; 
	
	
	public TopicDetail(TopicServiceAsync topicServiceAsync, TagServiceAsync tagServiceAsync){
		this.topicServiceA = topicServiceAsync;
		this.tagServiceA = tagServiceAsync;

		tagBoard = new TagBoard(topic, tagServiceAsync);


		detailPanel.setSpacing(4);		
		titleLabel.addStyleName("ta-compose-Label");
		titleBox.setWidth("28em");
		textArea.setCharacterWidth(50);
		textArea.setHeight("30em");

		topicTitlePanel.add(titleLabel);
		topicTitlePanel.add(titleBox);

		cancelButton.addClickListener(this);
		editTextButton.addClickListener(this);
		saveButton.addClickListener(this);

		detailPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		detailPanel.setWidth("100%");
		detailPanel.add(topicTitlePanel);
		detailPanel.add(textArea);
		detailPanel.add(saveButton);
		detailPanel.add(cancelButton);
		detailPanel.setCellWidth(titleBox,"100%");
		detailPanel.setCellWidth(textArea, "100%");

		//for preview
		buttonPanel.add(editTextButton);
		buttonPanel.add(saveButton);

		detailPanel.add(serverR);
		
		mainPanel.add(detailPanel);
		mainPanel.add(tagBoard);
		setWidget(mainPanel);
	}	

	public void load(Topic topic){
		this.topic = topic;
		setupTopic();		
		activateMainView();

		Effect.highlight(detailPanel);
	}

	private void setupTopic() {
		if(topic != null){
			titleBox.setText(topic.getTitle());
			textArea.setText(topic.getText());

			seeAlsoPanel.clear();
			seeAlsoStatic.clear();

			System.out.println("setup "+topic.getSeeAlso());
			if(topic.getSeeAlso() != null){
				Iterator i = topic.getSeeAlso().iterator();
				while (i.hasNext()) {
					Topic also = (Topic) i.next();
					System.out.println("add static "+also.getTitle());
					seeAlsoStatic.add(new TopicLink(also,this));					
				}
			}
			seeAlsoComplete = new SeeAlsoComplete(topic.getSeeAlso(),topicServiceA);
			
			mainPanel.remove(tagBoard);
			tagBoard = new TagBoard(topic, tagServiceA);
			tagBoard.load();
			mainPanel.add(tagBoard);
		}
	}

	public void onClick(Widget source){
		if (source == cancelButton){
			activateMainView();
		}
		else if (source == editTextButton){
			activateEditView();
		}
		else if (source == saveButton){
			topic.setText(textArea.getText());
			topic.setTitle(titleBox.getText());

			String[] seeAlsos = seeAlsoComplete.getText().split(seeAlsoComplete.SEPARATOR);

			System.out.println("save");
			for (int i = 0; i < seeAlsos.length; i++) {
				String string = seeAlsos[i];
				System.out.println("save array |"+string+"|");
			}
			
			tagBoard.saveValues();
			topic.setTags(tagBoard.getTags());

			topicServiceA.save(topic,seeAlsos,new StdAsyncCallback() {				

				public void onSuccess(Object result) {
					serverR.setText("Saved");
					activateMainView();
					topicList.load();
				}
			});	

		}
	}

	public void activateMainView(){

		textPanel.clear();
		textPanel.add(new TextDisplay(textArea.getText()));		
		titleEcho.setText(titleBox.getText());

		topicTitlePanel.clear();
		topicTitlePanel.add(titleLabel);
		topicTitlePanel.add(titleEcho);
		topicTitlePanel.add(new Label(" Updated: "+formatDate(topic.getLastUpdated())));

		seeAlsoPanel.clear();
		seeAlsoPanel.add(seeAlsoStatic);


		detailPanel.clear();
		detailPanel.add(topicTitlePanel);

		detailPanel.add(textPanel);

		detailPanel.add(seeAlsoPanel);

		detailPanel.add(buttonPanel);
	}

	public void activateEditView() {

		detailPanel.clear();
		
		topicTitlePanel.clear();
		topicTitlePanel.add(titleLabel);
		topicTitlePanel.add(titleBox);

		seeAlsoPanel.clear();
		seeAlsoPanel.add(seeAlsoComplete);		
		
		detailPanel.add(topicTitlePanel);

		detailPanel.add(textArea);

		detailPanel.add(seeAlsoPanel);

		detailPanel.add(cancelButton);
		detailPanel.add(saveButton);

	}

	public void setTopicServiceA(TopicServiceAsync topicServiceA) {
		this.topicServiceA = topicServiceA;
	}
	
	public void setTagServiceA(TagServiceAsync tagServiceA) {
		this.tagServiceA = tagServiceA;
	}

	public void setTopicList(TopicList topicList) {
		this.topicList = topicList;
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
