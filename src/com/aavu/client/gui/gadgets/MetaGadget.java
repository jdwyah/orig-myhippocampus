package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.Set;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.RemoveMetaFromTopicCommand;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.DeleteButton;
import com.aavu.client.widget.HeaderLabel;
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
		
		super();
		
		this.manager = _manager;
		this.type = type;
		
		
		metasPanel = new VerticalPanel();		
		
		mainPanel = new VerticalPanel();		
		
		PNGImage addEditButton = new PNGImage(ConstHolder.myConstants.img_add(),16,16);
		addEditButton.addMouseListener(new TooltipListener(ConstHolder.myConstants.meta_add_edit()));
		
		addEditButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				addEditClick();				
			}});
		
		HorizontalPanel headerP = new HorizontalPanel();
		
		headerP.add(new HeaderLabel(title));
		headerP.add(addEditButton);
		
		mainPanel.add(headerP);
		
		mainPanel.add(metasPanel);		
				
		
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
		
		Set tagBased = getTagBasedMetasFor(topic);
		
		for (Iterator iter = getAllMetasFor(topic).iterator(); iter.hasNext();) {
			Meta meta = (Meta) iter.next();		
			if(tagBased.contains(meta)){
				addMWidg(meta,false);
			}else{
				addMWidg(meta);
			}
		}		
		return 1;
	}	
	protected abstract Set getAllMetasFor(Topic topic);
	protected abstract Set getTagBasedMetasFor(Topic topic);

	private void addMWidg(final Meta meta) {
		addMWidg(meta, true);
	}
	private void addMWidg(final Meta meta,boolean showDelete) {
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
