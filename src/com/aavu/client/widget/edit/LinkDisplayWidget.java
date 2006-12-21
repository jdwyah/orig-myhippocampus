package com.aavu.client.widget.edit;

import java.util.Iterator;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.URI;
import com.aavu.client.domain.WebLink;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.ExternalLink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinkDisplayWidget extends Composite {
	
	private int size = 0;
	private VerticalPanel linkPanel;
	private Topic myTopic;
	private SaveNeededListener saveNeeded;
	private Manager manager;
	
	
	public LinkDisplayWidget(Manager _manager,SaveNeededListener saveNeeded) {
		this.saveNeeded = saveNeeded;
		this.manager = _manager;
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		linkPanel = new VerticalPanel();
		linkPanel.addStyleName("H-LinkDisplay");
				
		Button newLinkB = new Button(Manager.myConstants.link_add());		
		newLinkB.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				newLink();
			}
		});		
		
		mainPanel.add(newLinkB);
		mainPanel.add(linkPanel);
		
		initWidget(mainPanel);

	}
	
	private void newLink(){
		WebLink newL = new WebLink();
		newL.setUser(myTopic.getUser());
		
		editLink(newL);
	}
	private void editLink(WebLink link){
		AddLinkPopup pop = new AddLinkPopup(this,manager.newFrame(),link,myTopic,saveNeeded);
	}
	
	private void add(String description, String linkStr, String notes){
		WebLink link = new WebLink(myTopic.getUser(),
				description,linkStr,notes);
		
		myTopic.getOccurences().add(link);
		
		saveNeeded.onChange(this);
	}
	
	public void load(Topic topic){
		myTopic = topic;
		linkPanel.clear();
		size = 0;
		for (Iterator iter = topic.getOccurences().iterator(); iter.hasNext();) {
			Occurrence occ = (Occurrence) iter.next();			
			if(occ instanceof WebLink){
				HorizontalPanel lP = new HorizontalPanel();
				lP.add(new ExternalLink(occ));
				
				EditLinkButton editB = new EditLinkButton(Manager.myConstants.editMe(),(WebLink) occ);							
				lP.add(editB);				
				
				linkPanel.add(lP);
				
				size ++;
			}
		}
	}

	/**
	 * Button that remembers which Link to edit
	 * @author Jeff Dwyer
	 *
	 */
	private class EditLinkButton extends Button implements ClickListener{
		private WebLink link;

		public EditLinkButton(String text, WebLink link){
			super(text);
			this.link = link;
			addClickListener(this);
		}

		public void onClick(Widget sender) {
			editLink(link);	
		}
	}
	
	public int getSize() {
		return size;
	}

}
