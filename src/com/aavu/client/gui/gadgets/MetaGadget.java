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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	private VerticalPanel metasPanel;
	private VerticalPanel mainPanel;
	private Meta type;
	
	public MetaGadget(Manager _manager,String title,Meta type){		
		
		super(title);
		
		this.manager = _manager;
		this.type = type;
		
		
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
		
		System.out.println(topic.toPrettyString());
		
		metasPanel.clear();
		
		for (Iterator iter = getMetasFor(topic).iterator(); iter.hasNext();) {
			Meta meta = (Meta) iter.next();
			
			addMWidg(meta);
		}				
		return 1;
	}	
	protected abstract Set getMetasFor(Topic topic);


	private void addMWidg(final Meta meta) {
		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(meta.getEditorWidget(topic, manager));
		
		Button deleteButton = new Button("X");
		deleteButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				removeMeta(meta,topic,hp);
			}
		});
		hp.add(deleteButton);
		metasPanel.add(hp);
	}
	
	private void removeMeta(Meta meta, Topic topic, final Widget displayW) {
		manager.getTopicCache().executeCommand(topic, 
				new RemoveMetaFromTopicCommand(topic,meta),
				new StdAsyncCallback(ConstHolder.myConstants.meta_remove_async()){
					public void onSuccess(Object result) {
						super.onSuccess(result);
						metasPanel.remove(displayW);
					}});
	}			

	
}
