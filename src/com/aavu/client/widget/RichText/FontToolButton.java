/*
 * Created on 2006/07/29 18:11:21 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import java.util.Iterator;
import java.util.List;

public class FontToolButton extends DropDownToolButton {
	private List fonts;
	private DropDownMenu fontPanel;
	private boolean attached = false;
	public FontToolButton(String id, String imgUrl, String toolTip,
			String command) {
		super(id, imgUrl, toolTip, command);
	}

	public void beforeShowPanelEvent() {
		fontPanel.pain();
	}
	protected void onLoad(){
		super.onLoad();
		if(!attached){
			attachDropDown();
		}
	}
	private void attachDropDown(){
		RichTextAreaConfig cfg = this.getRichTextArea().getConfig();
		fonts = cfg.getFonts();
		fontPanel = new DropDownMenu(cfg.getDropDownPanelBackgroundColor(), 
				cfg.getDropDownPanelBorderColor(), 
				cfg.getMenuItemMouseOverBackgroundColor(), 
				cfg.getMenuItemMouseOverBorderColor());

		for(Iterator it = fonts.iterator(); it.hasNext(); ){
			String f = (String) it.next();
//Window.alert("Font tool button onload add " + f);			
			fontPanel.addMenuItem(f, "<font face=\"" + f + "\">" + f + "</font>");
		}
		fontPanel.addDropDownMenuListener(new DropDownMenuListener(){

			public void onClick(int row) {
			}

			public void onClick(String clickedItemValue) {
				restoreRange();
				hideDropDown();
				execCommand(getCommand(), clickedItemValue);
			}

			public void onShow() {
//				TODO: select the current font
				String v = getCommandValue();
				for(int i=0; i<fonts.size(); i++){
					String f = (String) fonts.get(i);
					if(v != null && f.equals(v)){
						fontPanel.setMenuItemChecked(i, true);
					}else{
						fontPanel.setMenuItemChecked(i, false);
					}
				}				
			}
		});
		
		this.addDropDownPanel(fontPanel);
		attached = true;
	}

	public void updateStates(Object newValue) {
		super.updateStates(newValue);
		if(!attached){
			attachDropDown();
		}
	}

}
