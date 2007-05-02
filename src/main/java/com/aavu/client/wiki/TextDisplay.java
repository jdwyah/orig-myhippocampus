package com.aavu.client.wiki;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;

public class TextDisplay extends Composite {

	public TextDisplay(String text) {
		//initWidget(new HTML(Translator.toHTML(text)));		
		initWidget(new HTML(text));
	}
	public TextDisplay(String text,int width, int height) {
		//initWidget(new HTML(Translator.toHTML(text)));		
		
		ScrollPanel scroll = new ScrollPanel(new HTML(text));
		scroll.setWidth(width+"px");
		scroll.setHeight(height+"px");
		initWidget(scroll);
	}

	
	
}
