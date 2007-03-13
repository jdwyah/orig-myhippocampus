package com.aavu.client.gui.gadgets;

import java.util.Iterator;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TimeGadget extends Gadget implements CloseListener {


	private Topic topic;
	private Manager manager;
	private VerticalPanel metasPanel;
	private VerticalPanel mainPanel;
	
	public TimeGadget(Manager _manager){		
		
		super(ConstHolder.myConstants.gadget_time_title());
		
		this.manager = _manager;
		
		
		metasPanel = new VerticalPanel();		
		
		mainPanel = new VerticalPanel();		
		
		Button addEditButton = new Button(ConstHolder.myConstants.meta_add_edit());
		addEditButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				addEditClick();				
			}});
		
		mainPanel.add(metasPanel);		
		mainPanel.add(addEditButton);		
		
		initWidget(mainPanel);		
		
		addStyleName("H-TimeGadget");		
		
	}

	private void addEditClick(){		
		manager.editMetas(new StdAsyncCallback(""){
			public void onSuccess(Object result) {			
				super.onSuccess(result);
				
				Meta m = (Meta) result;
				if(m != null){
					addMWidg(m);
				}
			}			
		},
		new MetaDate());		
	}
	
	//@Override
	public int load(Topic topic) {
		this.topic = topic;
		
		System.out.println(topic.toPrettyString());
		
		metasPanel.clear();
		
		for (Iterator iter = topic.getMetaDates().iterator(); iter.hasNext();) {
			MetaDate mDate = (MetaDate) iter.next();
			
//			ChooseMetaW metaChoose = new ChooseMetaW(manager,manager.getTopicCache(),mDate);
//			metaChoose.load(topic, mDate);
//			metasPanel.add(metaChoose);
			
			addMWidg(mDate);
		}				
		return 1;
	}	

	private void addMWidg(final Meta meta) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(meta.getEditorWidget(topic, manager));
		
		Button deleteButton = new Button("X");
		deleteButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				removeMeta(meta,topic);
			}
		});
		hp.add(deleteButton);
		metasPanel.add(hp);
	}
	
	private void removeMeta(Meta meta, Topic topic) {
//		manager.getTopicCache().executeCommand(topic, 
//				new,
//				callback)
	}			

	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_time(),40,60);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.gadget_time_title()));
		return b;
	}


	//@Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasTimeMetas();
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	

	

	
}
