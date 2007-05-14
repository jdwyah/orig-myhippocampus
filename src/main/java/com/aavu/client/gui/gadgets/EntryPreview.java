package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.aavu.client.widget.edit.TopicWidget;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class EntryPreview extends Gadget {

	private static final int NUM_CHARS = 240;

	private class EntryPreviewExt extends TopicWidget {
		//@Override
		public void setText(Entry entry) {		
			
			textPanel.clear();						
			
			Logger.log("entry "+entry.getData());
			Logger.log("chopBody "+entry.getDataWithoutBodyTags());
			String stripped = entry.getDataWithoutBodyTags();
			
			//TODO make sure we don't cut off in the middle of an HTML tag
			if(stripped != null && stripped.length() > NUM_CHARS){
				String str = stripped.substring(0,NUM_CHARS);
				str += "</p></div>";
				Logger.log("CUT |"+str+"|");
				textPanel.add(new TextDisplay(str));
			}else{
				Logger.log("NO CUT |"+stripped+"|");
				textPanel.add(new TextDisplay(stripped));
			}		
			
			System.out.println("textPanel "+textPanel);
		}
	}
	
	private EntryPreviewExt entryPrev;
	private Topic topic;
	private Manager manager;
	
	public EntryPreview(Manager _manager){		
		
		super(ConstHolder.myConstants.entry());
		
		this.manager = _manager;
		
		entryPrev = new EntryPreviewExt();
		
		entryPrev.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.editEntry(topic);				
			}});
	
		initWidget(entryPrev);
		
		
		addStyleName("H-EntryPreview");		
		
	}

	//@Override
	public int load(Topic topic) {
		this.topic = topic;
		entryPrev.load(topic);
		return 1;
	}

	//@Override
	public Image getPickerButton() {		
		Image b = ConstHolder.images.gadgetEntry().createImage();
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.entry()));
		return b;
	}

	//@Override
	public void showForFirstTime() {
		super.showForFirstTime();
		manager.editEntry(topic);
	}

	//@Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasEntry();
	}
	
	

	

	
}
