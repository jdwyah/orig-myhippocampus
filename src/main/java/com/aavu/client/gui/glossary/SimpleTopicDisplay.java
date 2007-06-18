package com.aavu.client.gui.glossary;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.maps.ext.HasClickListeners;
import com.aavu.client.gui.maps.ext.ListenerWidget;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SimpleTopicDisplay extends Composite implements HasClickListeners {

	private VerticalPanel mainP;
	
	private ClickListener listener;
	
	private Topic topic;
	private Manager manager;
	private CloseListener close;

	private Button goThere;

	private int width = -1;
	private int height = -1;
	
	
	public SimpleTopicDisplay() {
		mainP = new VerticalPanel();				
		initWidget(mainP);
		

		listener = new ClickListener(){
			public void onClick(Widget sender) {
				System.out.println("go there on click........"+manager+" "+close);
				manager.bringUpChart(topic.getId());
				close.close();					
			}};		
	}
	
	/**
	 * Pass a manager to enable the "Go There now" link.
	 * @param topic
	 */
	public SimpleTopicDisplay(Topic topic) {
		this(topic,null,null,-1,-1);
	}
	
	/**
	 * This CTOR enables to "Go There Now" link.
	 * @param topic
	 * @param manager
	 */
	public SimpleTopicDisplay(final Topic topic, final Manager manager, final CloseListener close,int width, int height) {
		this();	
		this.width = width;
		this.height = height;
		draw(topic,manager,close);
		
	}

	/**
	 * Draw after async request. Poke the callback when finished with the completed widget.
	 * 
	 * @param id
	 * @param manager
	 * @param close
	 * @param height 
	 * @param width 
	 */
	public SimpleTopicDisplay(final TopicIdentifier id, final Manager manager, final CloseListener close,int width, int height, final AsyncCallback callback) {
		this();	
		this.width = width;
		this.height = height;
		manager.getTopicCache().getTopicByIdA(id.getTopicID(), new StdAsyncCallback("Preview"){
			

			public void onSuccess(Object result) {
				super.onSuccess(result);				
				draw((Topic)result,manager,close);
				callback.onSuccess(SimpleTopicDisplay.this);
			}});
		
	}
	
	/**
	 * PEND MED move these init's someplace else, but careful not to break the async. 
	 * 
	 * @param topic
	 * @param manager
	 * @param close
	 */
	private void draw(final Topic topic, final Manager manager, final CloseListener close) {
		this.manager = manager;
		this.topic = topic;
		this.close = close;
		
		
		mainP.add(new HeaderLabel(topic.getTitle()));
		
		doEntry(topic);
		doTags(topic);
		doLinks(topic);
		
		if(manager != null){	
			goThere = new Button(ConstHolder.myConstants.topic_preview_gotherenow());
			goThere.addClickListener(listener);
			mainP.add(goThere);
		}				
	}

	private void doEntry(Topic topic) {
		Entry e = topic.getLatestEntry();
		
		if(!e.isEmpty()){
			if(width != -1 && height != -1){
				mainP.add(new TextDisplay(topic.getLatestEntry().getData(),width,height));	
			}else{
				mainP.add(new TextDisplay(topic.getLatestEntry().getData()));
			}
						
		}
	}

	private void doTags(Topic topic) {
		
		if(topic.getTags().size() > 0){
			mainP.add(new HeaderLabel(ConstHolder.myConstants.tags()));
			for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
				Topic tag = (Topic) iter.next();
				showTag(tag);						
			}		
		}
	}

	/**
	 * TODO not working when wrapped in GoogleMap. Needs GWTInfoWidgetWrapping?
	 * @param tag
	 */
	private void showTag(final Topic tag){
		
		TopicLink tagLink = new TopicLink(tag);				
		mainP.add(tagLink);	
		
		displayMetas(tag);
		
	}
	private void displayMetas(final Topic tag) {
		Set metas = tag.getTagProperties();		
//		for (Iterator iter = metas.iterator(); iter.hasNext();) {		
//			Meta element = (Meta) iter.next();
//			GWT.log("displayMetas", null);
//		
//			Widget w = element.getEditorWidget(cur_topic,saveNeeded,manager);
//			tagPanel.add(w);
//
//		}
	}


	private void doLinks(final Topic topic){		
		Set weblinks = topic.getWebLinks();
		if(!weblinks.isEmpty()){
			mainP.add(new HeaderLabel(ConstHolder.myConstants.occurrences()));
			for (Iterator iter = topic.getWebLinks().iterator(); iter.hasNext();) {
				WebLink link = (WebLink) iter.next();
				mainP.add(new ExternalLink(link));
			}
		}
	}

	public Widget getMainWidget() {
		return this;
	}

	public Set getPairs() {
		Set pairs = new HashSet();
		pairs.add(new ListenerWidget(listener,goThere));
		return pairs;
	}
	
}
