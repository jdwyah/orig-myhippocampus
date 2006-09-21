package com.aavu.client.widget.RichText2;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HippoEditor extends Editor {

	private ClickListener linkClickListener;

	public HippoEditor(ClickListener linkClickListener){
		super();
		this.linkClickListener = linkClickListener;
		setTextSize("600px", "400px");
	}

	//@Override
	protected void doOnInitSucess(){

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

		addFormatImageButton("link.gif", "createlink", "CreateLink",linkClickListener);
	}


	public void setText(String text) {
		System.out.println("SETTING HTML");
		setHTML(text);		
	}

	public String getText() {
		return getHTML();		
	}




	/**
	 * make a link
	 * 
	 * @param link
	 */
	public void makeLink(String link) {
		System.out.println("make link to "+link);	
		//expandSelection();		
		
		format("CreateLink", "#"+link);
	}


}
