package com.aavu.client.widget.edit;

import java.util.Iterator;

import com.aavu.client.HippoTest;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadBoard extends Composite {
		
	private VerticalPanel mainPanel = new VerticalPanel();
	private Topic topic;

	public UploadBoard(final Manager manager, Topic _topic) {
		this.topic = _topic;
		
		//don't let them upload to an unsaved topic
		//TODO make this appear after a save and give an indication that they need 
		//to save to make this appear.
		if(topic.getId() == 0){
			initWidget(mainPanel);
			return;
		}
		
		HorizontalPanel rowOne = new HorizontalPanel();
		rowOne.add(new HeaderLabel(Manager.myConstants.files()));
		Button addOne = new Button(Manager.myConstants.upload_add());
		addOne.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				UploadWidget widg = new UploadWidget(manager,topic,UploadBoard.this,HippoTest.realModuleBase+HippoTest.UPLOAD_PATH);		
				mainPanel.add(widg);			
			}});
		rowOne.add(addOne);		
		mainPanel.add(rowOne);
		
		System.out.println("UploadBoard OCCUR: "+topic.getOccurences().size());				
		for (Iterator iter = topic.getOccurences().iterator(); iter.hasNext();) {
			Occurrence occ = (Occurrence) iter.next();
			if(occ instanceof S3File){
				addS3File((S3File) occ);
			}
		}		
		
		initWidget(mainPanel);
	}
	public void addS3File(S3File file){
		
		mainPanel.add(new Label(file.getTitle()));
	}
}
