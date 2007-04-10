package com.aavu.client.widget;

import org.gwtwidgets.client.style.BorderStyle;
import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;


public class EnterInfoButton extends ImageButton {
	
	public EnterInfoButton(){
		super(ConstHolder.myConstants.enterInfo_image(),16,16);
		setBorderOnStyle(BorderStyle.BORDER_STYLE_NONE);
			
	}

}
