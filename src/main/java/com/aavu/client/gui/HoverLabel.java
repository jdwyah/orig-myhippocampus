/**
 * 
 */
package com.aavu.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Label Extension that will give you a hover css property.
 * 
 * 
 * .H-ContextMenu { border: 1px solid #000000; background-color: #eeeeee; }
 * 
 * .H-ContextMenu .gwt-Label.hover{ background-color: lime; }
 * 
 */
public class HoverLabel extends Composite {

	private static final String HOVER = "hover";

	private Label label;

	public HoverLabel(String text) {
		label = new Label(text);
		FocusPanel fp = new FocusPanel(label);
		fp.addMouseListener(new MouseListenerAdapter() {
			public void onMouseEnter(Widget sender) {
				label.addStyleName(HOVER);
			}

			public void onMouseLeave(Widget sender) {
				label.removeStyleName(HOVER);
			}
		});
		initWidget(fp);
	}
}