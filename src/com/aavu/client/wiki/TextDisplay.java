package com.aavu.client.wiki;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class TextDisplay extends Composite {

	public TextDisplay(String text) {
		setWidget(new HTML(Translator.toHTML(text)));
	}

	
	
}
