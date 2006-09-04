/*
 * Created on 2006/07/29 15:13:33 David
 */
package com.aavu.client.widget.RichText;


public class LinkToolButton extends ImageToolButton {

	public LinkToolButton(String id, String imgUrl, String toolTip) {
		super(id, imgUrl, toolTip, "createlink");
	}

	protected void onClick(){
		this.getRichTextArea().addLink();
	}
}
