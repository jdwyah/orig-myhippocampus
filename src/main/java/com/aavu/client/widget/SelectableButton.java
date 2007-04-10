package com.aavu.client.widget;

import com.google.gwt.user.client.ui.Button;

public class SelectableButton extends Button {

	public static final String UNSELECTED_BUTTON = "H-SelectableButton";
	public static final String SELECTED_BUTTON = "H-SelectableButton-Selected";
	private boolean selected;
	private ButtonGroup buttonGroup;
	
	public SelectableButton(String text, ButtonGroup buttonGroup){
		super(text);
		this.buttonGroup = buttonGroup;
		setStyleName(UNSELECTED_BUTTON);
	}

	public boolean isSelected() {
		return selected;
	}

	/**
	 * TODO GRR.. addStyle removeStyle still confuses me.  Just doesn't work sometimes. Styles
	 * get applied, but browser doesn't pick up on it. Help!
	 * 
	 * When this gets sorted, cleanup the css too.
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		System.out.println("SET SELECTED "+selected+" "+getText());
		if(selected){
			setStyleName(SELECTED_BUTTON);
		}else{
			setStyleName(UNSELECTED_BUTTON);
		}		
	}

	//@Override
	public void onClick() {		
		buttonGroup.newSelection(this);
	}
	
	
}
