package com.aavu.client.widget.edit;

import java.util.Iterator;

import org.gwtwidgets.client.ui.ImageButton;
import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.SaveOccurrenceCommand;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.ExternalLink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class LinkDisplayWidget extends Composite {
	
	private int size = 0;
	private VerticalPanel linkPanel;
	private Topic myTopic;	
	private Manager manager;
	
	
	public LinkDisplayWidget(Manager _manager) {		
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
		AddLinkPopup pop = new AddLinkPopup(this,manager,manager.newFrame(),link,myTopic);
	}
	
	private void add(String description, String linkStr, String notes){
		WebLink link = new WebLink(myTopic.getUser(),
				description,linkStr,notes);
		
		myTopic.getOccurences().add(link);
		
		manager.getTopicCache().save(new SaveOccurrenceCommand(myTopic.getId(), link),
				new StdAsyncCallback(Manager.myConstants.save()){});
		
	}
	
	public void load(Topic topic){
		myTopic = topic;
		linkPanel.clear();
		size = 0;
		for (Iterator iter = topic.getWebLinks().iterator(); iter.hasNext();) {
			WebLink occ = (WebLink) iter.next();			

			HorizontalPanel lP = new HorizontalPanel();
			lP.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
			
			lP.add(new ExternalLink(occ));

			EditLinkButton editB = new EditLinkButton((WebLink) occ);							
			lP.add(editB);			

			
			if(size %2 == 0){
				lP.addStyleName("H-LinkWidget");
			}else{
				lP.addStyleName("H-LinkWidget-Odd");	
			}
			linkPanel.add(lP);

			size ++;

		}
	}

	public int getSize() {
		return size;
	}
	
	/**
	 * Button that remembers which Link to edit
	 * @author Jeff Dwyer
	 *
	 */
	private class EditLinkButton extends PNGImage implements ClickListener{
		private WebLink link;

		public EditLinkButton(WebLink link){
			super("img/popupIcon.png", 15,15);
			this.link = link;
			addClickListener(this);
			addStyleName("H-EditLink-Button");			
		}

		public void onClick(Widget sender) {
			editLink(link);	
		}
	}
	

}
