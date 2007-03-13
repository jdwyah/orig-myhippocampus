package com.aavu.client.widget.tags;

import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Choose between MetaDate, MetaTopic & MetaText
 * Apply the selected name and return a new Meta object.
 * 
 * @author Jeff Dwyer
 *
 */
public class ChooseMetaW extends Composite {
		
	private ExistingMetasListBox metaType;
	
	private Manager manager;
	
	public ChooseMetaW(Manager manager,Meta type){
		this.manager = manager;
		HorizontalPanel mainP = new HorizontalPanel();
		

		metaType = new ExistingMetasListBox(type);
		
		
		manager.getTopicCache().getAllMetasOfType(type, new StdAsyncCallback(ConstHolder.myConstants.meta_lookup_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				metaType.addItems((List) result);		
			}});		
		mainP.add(metaType);
		
		
		
		initWidget(mainP);		
	}

	public void load(Topic t,Meta meta){

		metaType.select(meta);
		
	}	


	public Meta getSelectedMeta() {
		return metaType.getSelectedMeta();
	}

	public void add(Meta newM) {
		metaType.addItem(newM.getTitle(),newM);
	}
	
	
	
}
