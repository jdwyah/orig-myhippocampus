package com.aavu.client.widget.edit;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddLinkPopup extends PopupWindow {

	private static final int HEIGHT = 200;
	private static final int WIDTH = 550;
	private WebLink link;
	private Label descReq;
	private Label linkReq;

	public AddLinkPopup(final LinkDisplayWidget widget, GInternalFrame frame, WebLink _link, final Topic myTopic, final SaveNeededListener saveNeeded) {
		super(frame, Manager.myConstants.link_add_title(),WIDTH,HEIGHT);

		this.link = _link;
		
		Grid mainPanel = new Grid(4,3);
		
		mainPanel.setWidget(0, 0, new Label(Manager.myConstants.link_description()));		
		mainPanel.setWidget(1, 0, new Label(Manager.myConstants.link_url()));
		mainPanel.setWidget(2, 0, new Label(Manager.myConstants.link_notes()));
		
		descReq = new Label();
		linkReq = new Label();
		mainPanel.setWidget(0, 2, descReq);
		mainPanel.setWidget(1, 2, linkReq);
		
		final TextBox descriptionT = new TextBox();
		descriptionT.setSize("35em","2em");
		if(link.getDescription() != null){
			descriptionT.setText(link.getDescription());
		}
		final TextBox urlT = new TextBox();
		urlT.setSize("35em","2em");		
		if(link.getUri() != null){
			urlT.setText(link.getUri());
		}
		
		final TextArea notesT = new TextArea();
		notesT.setSize("30em", "7em");
		if(link.getNotes() != null){
			notesT.setText(link.getNotes());
		}
		
		mainPanel.setWidget(0, 1, descriptionT);
		mainPanel.setWidget(1, 1, urlT);
		mainPanel.setWidget(2, 1, notesT);
		
		Button submitB = new Button(Manager.myConstants.save());
		submitB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				
				if(descriptionT.getText().length() == 0){
					descReq.setText(Manager.myConstants.required());
					return;
				}
				if(urlT.getText().length() == 0){
					linkReq.setText(Manager.myConstants.required());
					return;
				}
				link.setDescription(descriptionT.getText());
				link.setNotes(notesT.getText());
				link.setUri(urlT.getText());
				
				if(link.getId() == 0){
					myTopic.getOccurences().add(link);
				}
				
				saveNeeded.onChange(widget);
				widget.load(myTopic);
				close();
			}});
		
		mainPanel.setWidget(3, 0, submitB);
		
		setContent(mainPanel);
	}

	
	
}
