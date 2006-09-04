/*
 * Created on 2006/07/27 18:06:51 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;


public class ImageToolButton extends CommandToolButton implements SwitchableToolButton{

	public ImageToolButton(String id, String imgUrl, String toolTip, String command) {
		super(id, "<img border=\"0\" src=\"" + imgUrl + "\" align=\"absmiddle\"/>", toolTip, command);
	}

	public void updateStates(Object newValue) {
		if(newValue == null){
			return;
		}
		this.setCommandValue((newValue == null) ? null : newValue.toString());
	}

}
