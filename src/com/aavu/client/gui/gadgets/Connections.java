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
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
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

public class Connections extends Gadget implements CompleteListener {

	private TopicCompleter topicCompleter;
	private TopicCache topicService;
	private Topic myTopic;
	
	private SeeAlsoWidget alsos;
	private Manager manager;
	private VerticalPanel refPanel;
	
	public Connections(Manager manager) {				
		
		super(Manager.myConstants.connections());
		
		this.manager = manager;
		
		topicService = manager.getTopicCache();
		
		topicCompleter = new TopicCompleter(manager.getTopicCache());		
		topicCompleter.addListener(this);

		alsos = new SeeAlsoWidget();
		
		EnterInfoButton enterInfoButton = new EnterInfoButton();		
		enterInfoButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				completed(topicCompleter.getText());
			}
		});

		
		VerticalPanel mainP = new VerticalPanel();
		
		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new Label(Manager.myConstants.addTo()));
		cp.add(topicCompleter);
		cp.add(enterInfoButton);
		
		
		refPanel = new VerticalPanel();
		
		mainP.add(cp);
		mainP.add(alsos);
		
		mainP.add(new Label(Manager.myConstants.references()));
		mainP.add(refPanel);
		
		initWidget(mainP);
		
	}

	public int load(Topic topic) {
		myTopic = topic;
				
	
		
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
		
		Association assoc = myTopic.getSeeAlsoAssociation();
		if(assoc == null){
			System.out.println("no see alsos");
			return 0;
		}else{
			return alsos.load(assoc);
		}
		
	}

	public void completed(String completeText) {
		
		topicService.getTopicIdentForNameOrCreateNew(completeText,new StdAsyncCallback(Manager.myConstants.seeAlso_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TopicIdentifier to = (TopicIdentifier) result;
				
				//myTopic.addSeeAlso(to);

				alsos.add(to);
				topicCompleter.setText("");
								
				topicService.save(myTopic,new SaveSeeAlsoCommand(myTopic,new Topic(to)),
						new StdAsyncCallback(Manager.myConstants.save_async()){});
				
			}});
		
	}
	
	/**
	 * display all the see also's as links to their topic.
	 * 
	 * @author Jeff Dwyer
	 */
	private class SeeAlsoWidget extends Composite {

		private CellPanel seeAlsoPanel;
		
		public SeeAlsoWidget(){			
			seeAlsoPanel = new VerticalPanel();			
			initWidget(seeAlsoPanel);
		}
		
		public int load(Association seeAlsoAssoc){
			int size = 0;
			seeAlsoPanel.clear();
			for (Iterator iter = seeAlsoAssoc.getMembers().iterator(); iter.hasNext();) {
				Topic top = (Topic) iter.next();
				seeAlsoPanel.add(new TopicLink(top.getIdentifier()));
				size++;
			}
			return size;
		}

		public void add(TopicIdentifier to2) {
			seeAlsoPanel.add(new TopicLink(to2));			
		}		
	}

	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(Manager.myConstants.img_gadget_connections(),60,60);
		b.addMouseListener(new TooltipListener(0,40,Manager.myConstants.connections()));
		return b;
	}

}
