package com.aavu.client.gui.gadgets;

import java.util.Iterator;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.LinkPlugin.AddLinkPopup;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.SaveOccurrenceCommand;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinkDisplayWidget extends Gadget implements TopicLoader {
	
	private static final String HEIGHT = "200px";
	private static final int USE_SCROLL_HEIGHT_CUTOFF = 6;
	
	private int size = 0;
	private VerticalPanel linkPanel;
	private Topic myTopic;	
	private Manager manager;
	
	
	public LinkDisplayWidget(Manager _manager) {		
		
		super(ConstHolder.myConstants.link_add_title());
		
		this.manager = _manager;
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		linkPanel = new VerticalPanel();
		linkPanel.addStyleName("H-LinkDisplay");
				
		Button newLinkB = new Button(ConstHolder.myConstants.link_add());		
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
				new StdAsyncCallback(ConstHolder.myConstants.save()){});
		
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

			
			table.setWidget(size, 0, new ExternalLink(occ,getPopupString(occ),true));
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
		//PEND awkward. we'd really like to set scroll.setMaxHeight(), 
		//but that doesn't exist. setting it in all cases leads to excess space
		ScrollPanel scroll = new ScrollPanel(table);
		if(size > USE_SCROLL_HEIGHT_CUTOFF){			
			scroll.setHeight(HEIGHT);					
		}
		linkPanel.add(scroll);
		return size;
	}

	private String getPopupString(WebLink occ) {
		StringBuffer popupText = new StringBuffer();
		popupText.append(occ.getUri());
		if(occ.getData() != null){
			popupText.append("<BR>");
			popupText.append(occ.getData());
		}							
		for (Iterator topicIter = occ.getTopics().iterator(); topicIter.hasNext();) {
			Topic top = (Topic) topicIter.next();
			if(top != myTopic){
				popupText.append("<BR>");
				popupText.append(top.getTitle());
			}
		}
		return popupText.toString();
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
			super(ConstHolder.myConstants.editMe());
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
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_link(),59,29);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.link_add_title()));
		return b;
	}
	

}
