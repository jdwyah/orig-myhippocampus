package com.aavu.client.widget.RichText2;

import org.gwtwidgets.client.wwrapper.WHyperlink;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

public class HippoEditor extends Editor {

	public HippoEditor(){
		super();
		setTextSize("600px", "400px");
		//sinkEvents(Event.KEYEVENTS);


	}

	//@Override
	protected void doOnInitSucess(){
		System.out.println("INIT SUCCESS adding link requestlisterner");
		FlexTable t;
		
		
		
		System.out.println("KEY "+Event.KEYEVENTS);
//		System.out.println("SUNK "+DOM.getEventsSunk(textElement));
//		System.out.println("SUNK! "+(DOM.getEventsSunk(textElement) | Event.KEYEVENTS));
//		
//		System.out.println("textElement "+textElement);
//		
//		System.out.println("text  "+text);
//		
//		
		//DOM.sinkEvents(text.getElement(), Event.KEYEVENTS
			//	| DOM.getEventsSunk(text.getElement()));
//
//		
		//text.addKeyboardListener(new LinkRequestListener());
		
		
		//WHyperlink;
		
		System.out.println("ADD editorW keyboar listener");
		
		editorW.addKeyboardListener(new KeyboardListener(){

			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				Window.alert("down");
				System.out.println("DOWN");
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				System.out.println("PRESS");				
			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				System.out.println("UP");
			}});

//
//		text.addFocusListener(new FocusListener(){
//
//			public void onFocus(Widget sender) {
//				System.out.println("focus!");
//			}
//
//			public void onLostFocus(Widget sender) {
//				System.out.println("lost focus!");				
//			}});
	}

	//@Override
	protected void addImages(){
		addFormatImageButton("bold.gif", "Bold", "Bold");
		addFormatImageButton("italic.gif", "Italic", "Italic");
		addFormatImageButton("underline.gif", "Underline", "Underline");

		addFormatImageButton("left.gif", "Left", "JustifyLeft");
		addFormatImageButton("center.gif", "Center", "JustifyCenter");
		addFormatImageButton("right.gif", "Right", "JustifyRight");

		addFormatImageButton("numbered.gif", "Numbered List", "InsertOrderedList");        
		addFormatImageButton("list.gif", "Bullet List", "InsertUnorderedList");
		addFormatImageButton("outdent.gif", "Outdent", "Outdent");        
		addFormatImageButton("indent.gif", "Indent", "Indent");        

		addFormatImageButton("hr.gif", "Horizontal Rule", "InsertHorizontalRule");

		addFormatImageButton("link.gif", "createlink", "CreateLink",new LinkClickListener());
	}


	public void setText(String text) {
		System.out.println("SETTING HTML");
		setHTML(text);		
	}

	public String getText() {
		return getHTML();		
	}


	private class LinkClickListener implements ClickListener {


		public void onClick(Widget sender) {		

			Hyperlink h;
			String link = "#Cathedral";

//			format(editorW.getElement(), "CreateLink", link);		
		}

	}

	private class LinkRequestListener extends KeyboardListenerAdapter {

		boolean isCtrl(int modifiers){
			return ((modifiers & KeyboardListener.MODIFIER_CTRL) == KeyboardListener.MODIFIER_CTRL);
		}

		public void onKeyUp(Widget sender, char keyCode, int modifiers) {

			System.out.println("key rec");

			if(keyCode == 'L' && isCtrl(modifiers)){
				System.out.println("ctrl-l");
//				TextArea ta = (TextArea) sender;

//				System.out.println("cur "+ta.getCursorPos());
//				System.out.println("len "+ta.getSelectionLength());

				Object selected = getSelectionRange();

				System.out.println(selected);

//				if(ta.getSelectedText().equals("")){

//				int cursor = ta.getCursorPos();

//				String s = ta.getText();

//				pre = s.lastIndexOf(" ", cursor);
//				if(pre == -1){
//				pre = 0;
//				}
//				int p = s.indexOf(" ",cursor);
//				post = s.length();		    		  
//				if(p != -1){
//				post = p; 
//				}
//				System.out.println("pre "+pre+" p "+p+" post "+post);
//				selected = s.substring(pre, post);
//				System.out.println("last word: "+selected);




//				completePanel.setVisible(true);				
//				Effect.appear(completePanel);

//				//Effect.highlight(completePanel);

//				DeferredCommand.add(new Command(){
//				public void execute() {				
//				completer.setFocus(true);		
//				}});
//				}


			}
		}

	}

}
