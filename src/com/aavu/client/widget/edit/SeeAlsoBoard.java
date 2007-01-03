package com.aavu.client.widget.edit;

import java.util.Iterator;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Association;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SeeAlsoBoard extends Composite implements CompleteListener {

	private TopicCompleter topicCompleter;
	private TopicCache topicService;
	private Topic myTopic;
	
	private SeeAlsoWidget alsos;
	private SaveNeededListener saveNeeded;
	private PopupWindow popwindow;
	
	public SeeAlsoBoard(Manager manager, SaveNeededListener saveNeeded,PopupWindow popwindow) {
		this.saveNeeded = saveNeeded;
		this.popwindow = popwindow;
		topicService = manager.getTopicCache();
		
		topicCompleter = new TopicCompleter();		
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
		cp.add(new HeaderLabel(Manager.myConstants.seeAlsos()));
		cp.add(topicCompleter);
		cp.add(enterInfoButton);
		
		mainP.add(cp);
		mainP.add(alsos);
		
		
		initWidget(mainP);
	}

	public int load(Topic topic) {
		myTopic = topic;
				
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
				
				myTopic.addSeeAlso(to);

				alsos.add(to);
				topicCompleter.setText("");
				saveNeeded.onChange(SeeAlsoBoard.this);
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
				seeAlsoPanel.add(new TopicLink(top.getIdentifier(),popwindow));
				size++;
			}
			return size;
		}

		public void add(TopicIdentifier to2) {
			seeAlsoPanel.add(new TopicLink(to2,popwindow));			
		}		
	}

}
