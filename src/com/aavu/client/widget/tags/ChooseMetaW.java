package com.aavu.client.widget.tags;

import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * List all existing metas of the given type
 * 
 * @author Jeff Dwyer
 *
 */
public class ChooseMetaW extends Composite implements ChangeListener {
		
	private static final int VIS_ITEM_COUNT = 6;

	private ExistingMetasListBox metaType;
	
	private Manager manager;

	private ChangeListenerCollection listeners;
	
	public ChooseMetaW(Manager manager,Meta type){
		this.manager = manager;
		HorizontalPanel mainP = new HorizontalPanel();
		

		metaType = new ExistingMetasListBox(type);
		metaType.setVisibleItemCount(VIS_ITEM_COUNT);
		
		manager.getTopicCache().getAllMetasOfType(type, new StdAsyncCallback(ConstHolder.myConstants.meta_lookup_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				metaType.addItems((List) result);		
			}});		
		mainP.add(metaType);
		
		initWidget(mainP);		
	}
	public void addChangeListener(ChangeListener listener) {
		metaType.addChangeListener(this);
		if(listeners == null){
			listeners = new ChangeListenerCollection();
		}
		listeners.add(listener);
	}

	public Meta getSelectedMeta() {
		return metaType.getSelectedMeta();
	}

	public void add(Meta newM) {
		metaType.addItem(newM.getTitle(),newM);
	}
	public void onChange(Widget sender) {
		if(listeners != null){
			listeners.fireChange(this);	
		}
	}
	
	
	
}
