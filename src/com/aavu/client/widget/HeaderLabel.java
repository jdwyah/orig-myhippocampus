package com.aavu.client.widget;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.Dashboard;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HeaderLabel extends Composite {

	public HeaderLabel(String string) {
		this(string,false);
	}

	public HeaderLabel(String string, boolean question) {
		Label l = new Label(string);		
		l.setStyleName("H-HeaderLabel");	
		
		if(question){
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(l);
			
			ImageButton help = new ImageButton(Manager.myConstants.question_img_src(),13,13);
			help.addMouseListener(new TooltipListener("Help me!"));
			hp.add(help);
					
			initWidget(hp);
		}else{			
			initWidget(l);	
		}
		
		
	}
}
