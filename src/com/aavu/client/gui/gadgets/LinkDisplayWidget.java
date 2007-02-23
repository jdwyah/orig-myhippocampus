package com.aavu.client.gui.gadgets;

import java.util.Iterator;

import org.gwtwidgets.client.ui.ImageButton;
import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.SaveOccurrenceCommand;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.edit.AddLinkPopup;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class LinkDisplayWidget extends Gadget {
	
	private int size = 0;
	private VerticalPanel linkPanel;
	private Topic myTopic;	
	private Manager manager;
	
	
	public LinkDisplayWidget(Manager _manager) {		
		
		super(Manager.myConstants.link_add_title());
		
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
		
		//myTopic.getOccurences().add(link);
		
		manager.getTopicCache().save(myTopic,new SaveOccurrenceCommand(myTopic, link),
				new StdAsyncCallback(Manager.myConstants.save()){});
		
	}
	
	public int load(Topic topic){
		myTopic = topic;
		linkPanel.clear();
		size = 0;
		FlexTable table = new FlexTable();

		for (Iterator iter = topic.getWebLinks().iterator(); iter.hasNext();) {
			WebLink occ = (WebLink) iter.next();			

			
					
//			HorizontalPanel lP = new HorizontalPanel();
//			lP.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
//			
//			lP.add(new ExternalLink(occ));
//
			EditLinkButton editB = new EditLinkButton((WebLink) occ);							
//			lP.add(editB);			

			table.setWidget(size, 0, new ExternalLink(occ));
			table.setWidget(size, 1, editB);
			
						
			if(size %2 == 0){
//				lP.addStyleName("H-LinkWidget");
				table.getFlexCellFormatter().addStyleName(size,0,"H-LinkWidget");
			}else{
//				lP.addStyleName("H-LinkWidget-Odd");	
				table.getFlexCellFormatter().addStyleName(size,0,"H-LinkWidget-Odd");
			}			
			
//			linkPanel.add(lP);

			size ++;

		}
		
		linkPanel.add(table);
		return size;
	}

	public int getSize() {
		return size;
	}
	
	/**
	 * Button that remembers which Link to edit
	 * @author Jeff Dwyer
	 *
	 */
	private class EditLinkButton extends Button implements ClickListener{
		private WebLink link;

		public EditLinkButton(WebLink link){
			//super("img/popupIcon.png", 15,15);
			super(Manager.myConstants.editMe());
			this.link = link;
			addClickListener(this);
			addStyleName("H-EditLink-Button");			
		}

		public void onClick(Widget sender) {
			editLink(link);	
		}
	}

	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(Manager.myConstants.img_gadget_link(),59,29);
		b.addMouseListener(new TooltipListener(0,40,Manager.myConstants.link_add_title()));
		return b;
	}
	

}
