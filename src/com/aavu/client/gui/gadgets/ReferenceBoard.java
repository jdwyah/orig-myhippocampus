package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Association;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveSeeAlsoCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.AllReferencesPanel;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReferenceBoard extends Gadget {

	private Manager manager;

	private VerticalPanel refPanel;
		
	public ReferenceBoard(Manager manager) {				
		
		super(Manager.myConstants.references());
		
		this.manager = manager;
	
	
		VerticalPanel mainP = new VerticalPanel();
		
		refPanel = new VerticalPanel();
		
		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new Label(Manager.myConstants.references()));
		
		mainP.add(cp);
		mainP.add(refPanel);
		
		
		initWidget(mainP);
		
	}

	public int load(Topic topic) {
				
		manager.getTopicCache().getLinksTo(topic,new StdAsyncCallback("GetLinksTo"){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				List list = (List) result;
				
				refPanel.clear();
				
				if(list.size() == 0){
					refPanel.add(new Label(Manager.myConstants.references_none()));					
				}
				
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TopicIdentifier topicIdent = (TopicIdentifier) iter.next();
					refPanel.add(new TopicLink(topicIdent));
				}
				
				//updateTitle(refPanel,Manager.myConstants.referencesN(list.size()));				
				//bar.updateTitle(AllReferencesPanel.this,Manager.myConstants.all_referencesN(totalSize + list.size()));
			}
			
		});
		
		return 0;
	}

	//@Override
	public ImageButton getPickerButton() {
		// TODO Auto-generated method stub
		return null;
	}
}
