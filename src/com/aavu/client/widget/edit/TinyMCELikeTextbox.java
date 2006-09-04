package com.aavu.client.widget.edit;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class TinyMCELikeTextbox extends HTML implements KeyboardListener, MouseListener{

	private String pre = "<HTML><SPAN STYLE=\"font-size: larger;\">";

	private String post = "</SPAN></HTML>";

	private StringBuffer sb;

	HTML thHTML = new HTML();

	boolean bold;
	int cursor;

	private CharSequence bold_pre = "<SPAN STYLE=\"font-weight: bold;\">";
	private CharSequence bold_post = "</SPAN>";
	
	public TinyMCELikeTextbox(){
		sb = new StringBuffer(pre);
		sb.append("asdfsdafsd<p>sdfsdfsfd<p>fooooooooooo");
		sb.append(post);

		cursor = pre.length() + 1;
		
		thHTML.setHTML(sb.toString());


		setHTML(getHTML());
	}


	public String getHTML() { 	
		return sb.toString();
	}

	boolean isCtrl(int modifiers){
		return ((modifiers & KeyboardListener.MODIFIER_CTRL) == KeyboardListener.MODIFIER_CTRL);
	}


	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub

	}


	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub

	}


	public void onKeyUp(Widget sender, char keyCode, int modifiers) {

		//getParent().
		System.out.println("key up "+sb.toString());		
		
		if(keyCode == 'B' && isCtrl(modifiers)){
		System.out.println("bold! ");
			if(bold){
				
				cursor += bold_post.length();
				bold = false;
			}else{
				sb.insert(cursor, bold_pre);
				cursor += bold_pre.length();
				sb.insert(cursor, bold_post);				
				bold = true;
			}
			
		}else{
			sb.insert(cursor, keyCode);
			cursor++;
		}

		setHTML(sb.toString());
		
		
		if(keyCode == 'L' && isCtrl(modifiers)){
			System.out.println("ctrl-l");
			TextArea ta = (TextArea) sender;

			String selected = ta.getSelectedText();

			if(ta.getSelectedText().equals("")){

				int cursor = ta.getCursorPos();

				String s = ta.getText();

				int pre = s.lastIndexOf(" ", cursor);
				if(pre == -1){
					pre = 0;
				}
				int p = s.indexOf(" ",cursor);
				int post = s.length();		    		  
				if(p != -1){
					post = p; 
				}
				System.out.println("pre "+pre+" p "+p+" post "+post);
				selected = s.substring(pre, post);
				System.out.println("last word: "+selected);

			}


		}
	}


	public void onMouseDown(Widget sender, int x, int y) {
		// TODO Auto-generated method stub
		
	}


	public void onMouseEnter(Widget sender) {
		// TODO Auto-generated method stub
		
	}


	public void onMouseLeave(Widget sender) {
		// TODO Auto-generated method stub
		
	}


	public void onMouseMove(Widget sender, int x, int y) {
		// TODO Auto-generated method stub
		
	}


	public void onMouseUp(Widget sender, int x, int y) {
		// TODO Auto-generated method stub
		System.out.println("x :"+x+" y "+y);
	}	
}
