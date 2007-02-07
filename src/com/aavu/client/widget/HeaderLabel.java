package com.aavu.client.widget;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HeaderLabel extends Composite {

	private Label l;

	public HeaderLabel(String string) {
		this(string,null);
	}

	public HeaderLabel(String string, final String helpText) {
		l = new Label(string);		
		l.setStyleName("H-HeaderLabel");	
		
		if(null != helpText){
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(l);
			
			ImageButton help = new ImageButton(Manager.myConstants.question_img_src(),13,13);
			help.addMouseListener(new TooltipListener("Help me!"));
			help.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					Window.alert(helpText);					
				}				
			});
			hp.add(help);
					
			initWidget(hp);
		}else{			
			initWidget(l);	
		}			
	}
	
	public void setText(String text){
		l.setText(text);
	}
}
