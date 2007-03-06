package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.TopicWidget;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class EntryPreview extends Gadget {

	private static final int NUM_CHARS = 240;

	private class EntryPreviewExt extends TopicWidget {
		//@Override
		public void setText(Entry entry) {		
			
			textPanel.clear();						
			
			System.out.println("entry "+entry.getData());
			
			//TODO make sure we don't cut off in the middle of an HTML tag
			if(entry.getData() != null && entry.getData().length() > NUM_CHARS){
				String str = entry.getData().substring(0,NUM_CHARS)+"</body>";
				System.out.println(str);
				textPanel.add(new TextDisplay(str));
			}else{
				textPanel.add(new TextDisplay(entry.getData()));
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
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_entry(),60,47);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.entry()));
		return b;
	}

	//@Override
	public void showForFirstTime() {
		super.showForFirstTime();
		manager.editEntry(topic);
	}
	
	

	

	
}
