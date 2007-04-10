/*
 * Created on 2006/07/26 11:01:54 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import com.google.gwt.user.client.ui.Widget;

public abstract class ToolButton extends Widget {
	private String id;
	protected RichTextArea richTextArea;
	private Object range;
	
	
	public ToolButton(){
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RichTextArea getRichTextArea() {
		return richTextArea;
	}

	public void setRichTextArea(RichTextArea editor) {
		this.richTextArea = editor;
	}
	

	public void execCommand(String command, String value){
		richTextArea.execCommand(command, value);
		//update tool button states
		richTextArea.updateToolButtonStates();
	}

	protected void saveRange(){
		range = this.getRichTextArea().getSelectionRange();
	}
	protected void restoreRange(){
		this.getRichTextArea().setSelectionRange(range);
	}

}
