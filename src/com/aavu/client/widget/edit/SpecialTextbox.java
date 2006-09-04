package com.aavu.client.widget.edit;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.widget.RichText.RichTextArea;
import com.aavu.client.widget.RichText2.Editor;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SpecialTextbox extends Composite implements KeyboardListener {

	private Editor textArea;
	private VerticalPanel completePanel;
	private TopicCompleter completer;
	private Button selectButton;
	private int post;
	private int pre;
	private String selected;


	//private List 


	public SpecialTextbox(){
		super();

		textArea = new Editor();
		textArea.setTextSize("600px", "500px");
		

	//	textArea.addKeyboardListener(this);

		HorizontalPanel mainPanel = new HorizontalPanel();		

		completePanel = new VerticalPanel();
		completer = new TopicCompleter();

		selectButton = new Button("Link");
		selectButton.addClickListener(new LinkItListener());

		completePanel.add(completer);
		completePanel.add(selectButton);
		completePanel.setVisible(false);

		mainPanel.add(textArea);
		

		
		mainPanel.add(completePanel);
		initWidget(mainPanel);
	}


	boolean isCtrl(int modifiers){
		return ((modifiers & KeyboardListener.MODIFIER_CTRL) == KeyboardListener.MODIFIER_CTRL);
	}


	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
	}
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
	}
	public void onKeyUp(Widget sender, char keyCode, int modifiers) {


		if(keyCode == 'L' && isCtrl(modifiers)){
			System.out.println("ctrl-l");
			TextArea ta = (TextArea) sender;

			System.out.println("cur "+ta.getCursorPos());
			System.out.println("len "+ta.getSelectionLength());

			selected = ta.getSelectedText();

			if(ta.getSelectedText().equals("")){

				int cursor = ta.getCursorPos();

				String s = ta.getText();

				pre = s.lastIndexOf(" ", cursor);
				if(pre == -1){
					pre = 0;
				}
				int p = s.indexOf(" ",cursor);
				post = s.length();		    		  
				if(p != -1){
					post = p; 
				}
				System.out.println("pre "+pre+" p "+p+" post "+post);
				selected = s.substring(pre, post);
				System.out.println("last word: "+selected);




				completePanel.setVisible(true);				
				Effect.appear(completePanel);

				//Effect.highlight(completePanel);

				DeferredCommand.add(new Command(){
					public void execute() {				
						completer.setFocus(true);		
					}});
			}


		}
	}


	public void setText(String text) {
		textArea.setHTML(text);		
	}

	public String getText() {
		return textArea.getHTML();
		
	}


	/**
	 * What to do when they click link or hit enter in the lookup
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class LinkItListener implements ClickListener {

		public void onClick(Widget sender) {

//			String linkTo = completer.getText();
//
//			//textArea.setFocus(true);
//			
//			StringBuffer sb = new StringBuffer(textArea.getHtml());
//			sb.insert(pre, "[");
//			pre++;
//
//			sb.insert(pre, linkTo);
//			pre += linkTo.length();
//
//			sb.insert(pre, ",");
//			pre++;
//
//			pre += selected.length();
//			sb.insert(pre, "]");
//
//			pre++;
//
//			textArea.setHtml(sb.toString());
//			Effect.dropOut(completePanel);
		}
	}

}
