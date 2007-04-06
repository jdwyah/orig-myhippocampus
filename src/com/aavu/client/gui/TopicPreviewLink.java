package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.glossary.SimpleTopicDisplay;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Cool class that prevents us sending millions of async requests on mouse over,
 * delaying just a bit so we're sure the user really wants it. 
 * 
 * @author Jeff Dwyer
 *
 */
public class TopicPreviewLink extends TopicLink implements MouseListener {

	private Panel showPreview;
	private Manager manager;
	private boolean dirty = true;
	private Timer t;
	
	public TopicPreviewLink(TopicIdentifier topic,int max_link_chars,CloseListener popup,Panel showPreview,Manager manager){
		super(topic,popup,max_link_chars);		
		addMouseListener(this);
		this.showPreview = showPreview;
		this.manager = manager;
	}

	private void showPreview(){
		manager.getTopicCache().getTopicByIdA(id, new StdAsyncCallback("Preview"){
			public void onSuccess(Object result) {
				super.onSuccess(result);

				Topic topic = (Topic) result;
				showPreview.clear();
				SimpleTopicDisplay display = new SimpleTopicDisplay(topic);
				
				showPreview.add(display);
				GUIEffects.highlight(display);
				
				dirty = false;
			}});
	}


	public void onMouseEnter(Widget sender) {
		if(dirty){
			t = new Timer(){
				public void run() {
					showPreview();
					dirty = false;		
				}};
			
			//System.out.println("schedule "+id);
			t.schedule(500);

		}
	}


	public void onMouseLeave(Widget sender) {
		//System.out.println("cancel "+id);
		t.cancel();
		dirty = true;		
	}

	public void onMouseDown(Widget sender, int x, int y) {}
	public void onMouseMove(Widget sender, int x, int y) {}
	public void onMouseUp(Widget sender, int x, int y) {}



}
