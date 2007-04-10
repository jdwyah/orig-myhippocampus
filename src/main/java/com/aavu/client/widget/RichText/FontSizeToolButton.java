/*
 * Created on 2006/07/29 18:50:05 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;



public class FontSizeToolButton extends DropDownToolButton {
	private DropDownMenu fontSizePanel;
	private boolean attached = false;
	
	public FontSizeToolButton(String id, String imgUrl, String toolTip,
			String command) {
		super(id, imgUrl, toolTip, command);
	}

	protected void onLoad(){
		super.onLoad();
		if (!attached){
			attachDropDown();
		}
	}
	private void attachDropDown(){
		RichTextAreaConfig cfg = this.getRichTextArea().getConfig();
		final String[][] fontSizes = cfg.getFontSizes();
		fontSizePanel = new DropDownMenu(cfg.getDropDownPanelBackgroundColor(), 
				cfg.getDropDownPanelBorderColor(), 
				cfg.getMenuItemMouseOverBackgroundColor(), 
				cfg.getMenuItemMouseOverBorderColor());

		for(int i=0; i<fontSizes.length; i++){
			fontSizePanel.addMenuItem(fontSizes[i][0], "<font size=\"" + fontSizes[i][0] + "\">" + fontSizes[i][1] + "</font>");
		}
		fontSizePanel.addDropDownMenuListener(new DropDownMenuListener(){

			public void onClick(int row) {
			}

			public void onClick(String clickedItemValue) {
				restoreRange();
				hideDropDown();
				execCommand(getCommand(), clickedItemValue);
			}

			public void onShow() {
				//TODO: select the current font-size
				String v = getCommandValue();
				for(int i=0; i<fontSizes.length; i++){
					String size = fontSizes[i][0];
					if(v != null && size.equals(v)){
						fontSizePanel.setMenuItemChecked(i, true);
					}else{
						fontSizePanel.setMenuItemChecked(i, false);
					}
				}
			}
		});
		
		this.addDropDownPanel(fontSizePanel);
		attached = true;
	}

	protected void beforeShowPanelEvent() {
		fontSizePanel.pain();
	}
	
	public void updateStates(Object newValue) {
		super.updateStates(newValue);
		if(!attached){
			attachDropDown();
		}
	}


	
}
