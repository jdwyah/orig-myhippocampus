package com.aavu.client.widget.edit;

import com.aavu.client.widget.RichText2.HippoEditor;
import com.aavu.client.widget.RichText2.KeyCodeEventListener;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SpecialTextbox extends Composite implements KeyCodeEventListener, ClickListener {
	
	private static final int KEY_PIPE = 124;
	
	private HippoEditor textArea;
	private PopupPanel completePopup;
	private TopicCompleter completer;
	private Button selectButton;
	private int post;
	private int pre;
	private String selected;

	private JavaScriptObject range;


	//private List 


	public SpecialTextbox(){
		super();

		textArea = new HippoEditor(this);
		textArea.setKeyEventlistener(this);
		
		HorizontalPanel mainPanel = new HorizontalPanel();		

		
		completer = new TopicCompleter();

		selectButton = new Button("Link");
		selectButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				link();
			}});

		
		completePopup = new PopupPanel();
		completePopup.setPopupPosition(300,300);
		
		HorizontalPanel completePanel = new HorizontalPanel();
		completePanel.add(completer);
		completePanel.add(selectButton);			
		completePopup.add(completePanel);

		mainPanel.add(textArea);
		

		
		//mainPanel.add(completePanel);
		initWidget(mainPanel);
	}




	public void keyCodeEvent(int i) {
		if(i == KEY_PIPE){
			openLinkDialog();			
		}
	}


	public void setText(String text) {
		textArea.setHTML(text);		
	}

	public String getText() {
		return textArea.getHTML();
		
	}

	private void link(){

		String linkTo = completer.getText();

		textArea.setSelectionRange(range);
		textArea.makeLink(linkTo);
		
		//Effect.dropOut(completePopup);
		
		completePopup.hide();
		
		textArea.setFocus(true);
	}

	public void onClick(Widget sender) {
		openLinkDialog();
	}




	private void openLinkDialog() {
		System.out.println("LINK");

		range = textArea.getSelectionRange();
		
		completePopup.show();			
//		completePopup.setVisible(true);				
//		Effect.appear(completePopup);

		//Effect.highlight(completePanel);

		DeferredCommand.add(new Command(){
			public void execute() {				
				completer.setFocus(true);		
			}});
		
	}

}
