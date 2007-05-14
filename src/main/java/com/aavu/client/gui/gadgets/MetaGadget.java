package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.Set;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.RemoveMetaFromTopicCommand;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.AddButton;
import com.aavu.client.widget.DeleteButton;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Jeff Dwyer
 *
 */
public abstract class MetaGadget extends Gadget  {

	private Topic topic;
	private Manager manager;
	private VerticalPanel mainPanel;
	
	protected VerticalPanel metasPanel;	
	protected Meta type;
	protected SimplePanel extraPanel;
	
	public MetaGadget(Manager _manager,String title,Meta type){		
		
		super();
		
		this.manager = _manager;
		this.type = type;
		
		
		metasPanel = new VerticalPanel();		
		
		mainPanel = new VerticalPanel();	
		extraPanel = new SimplePanel();
				
		AddButton addEditButton = new AddButton(ConstHolder.myConstants.meta_add_edit());
		
		addEditButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				addEditClick();		
				
				//PEND MED grrr... DisclosurePanel closes if we have any click spots. Re-open
				//automatically to counteract, but this ends up with a flicker.
				DeferredCommand.addCommand(new Command(){
					public void execute() {
						mainP.setOpen(true);		
					}});				
			}});
		
		HorizontalPanel headerP = new HorizontalPanel();
		
		headerP.add(new HeaderLabel(title));
		headerP.add(addEditButton);
		
		setHeader(headerP);
		
		mainPanel.add(metasPanel);		
		mainPanel.add(extraPanel);
						
		initWidget(mainPanel);					
		
	}

	private void addEditClick(){		
		manager.editMetas(new EZCallback(){
			public void onSuccess(Object result) {			
				
				Meta m = (Meta) result;
				if(m != null){
					addMWidg(m);
				}
			}			
		},
		type);		
	}
	
	//@Override
	public int load(Topic topic) {
		this.topic = topic;
		
		//System.out.println("Meta Gadget Load "+topic.toPrettyString());
		
		metasPanel.clear();
		
		Set tagBased = getTagBasedMetasFor(topic);
		int size = 0;
		for (Iterator iter = getAllMetasFor(topic).iterator(); iter.hasNext();) {
			Meta meta = (Meta) iter.next();		
			if(tagBased.contains(meta)){
				addMWidg(meta,false);
			}else{
				addMWidg(meta);
			}
			size++;
		}		
		return size;
	}	
	private Set getAllMetasFor(Topic topic){
		return topic.getAllMetas(type);
	}
	private Set getTagBasedMetasFor(Topic topic){
		return topic.getTagPropertyBasedMetas(type);
	}
	public boolean isOnForTopic(Topic topic) {
		return topic.hasMetas(type);
	}
	private void addMWidg(final Meta meta) {
		addMWidg(meta, true);
	}
	protected void addMWidg(final Meta meta,boolean showDelete) {
		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(meta.getEditorWidget(topic, manager));
		
		if(showDelete){
			DeleteButton deleteButton = new DeleteButton();
			deleteButton.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					removeMeta(meta,topic,hp);
				}
			});
			hp.add(deleteButton);
		}
		metasPanel.add(hp);
	}
	
	protected void removeMeta(Meta meta, Topic topic, final Widget displayW) {
		manager.getTopicCache().executeCommand(topic, 
				new RemoveMetaFromTopicCommand(topic,meta),
				new StdAsyncCallback(ConstHolder.myConstants.meta_remove_async()){
					public void onSuccess(Object result) {
						super.onSuccess(result);
						metasPanel.remove(displayW);
					}});
	}			

	
}
