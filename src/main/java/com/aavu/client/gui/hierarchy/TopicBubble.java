package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TopicBubble extends AbstractBubble implements Bubble, ClickListener {
	
	

	private FullTopicIdentifier fti;

	public TopicBubble(FullTopicIdentifier fti,HierarchyDisplay display) {
		super(fti.getLongitudeOnIsland(),
				fti.getLatitudeOnIsland(),
				fti.getTopicTitle(),
				new Image(ImageHolder.getImgLoc("hierarchy/")+"ball_white.png"),
				display);
		
	
		this.fti = fti;
		
		setDropController(new BubbleDropController(this));
		
		addClickListener(this);
		
	}


	

	public FocusPanel getFocusPanel() {
		return this;
	}

	public FullTopicIdentifier getFTI() {
		return fti;
	}

	

	/**
	 * NOTE: just wrapping the FTI. Not a fully loaded topic.
	 * @return
	 */
	public Topic getTopic() {
		return new Topic(getFTI());
	}

	public void grow() {
		// TODO Auto-generated method stub
		
	}

	public void onClick(Widget sender) {
		
		getDisplay().navigateTo(getFTI());
	}

	

	public void receivedDrop(Bubble bubble) {

		if(bubble instanceof TopicBubble){
			TopicBubble received = (TopicBubble)bubble;
			getDisplay().removeTopicBubble(received);
			getDisplay().getManager().getTopicCache().executeCommand(received.getTopic(),new SaveTagtoTopicCommand(received.getTopic(),getTopic(),getDisplay().getCurrentRoot()), 
					new StdAsyncCallback(ConstHolder.myConstants.save()){
				//@Override
				public void onSuccess(Object result) {
					super.onSuccess(result);						
				}			
			});
		} else {
			Window.alert("can't dnd that yet");			
		}
	}

	public void receivedDrop(Widget draggable) {
		TopicBubble received = (TopicBubble) draggable;
		
		//display.processDrop(this,received);
		
		
	}

	//@Override
	protected void saveLocation() {
		getDisplay().getManager().getTopicCache().saveTopicLocationA(getDisplay().getCurrentRoot().getId(), getFTI().getTopicID(), getTop(), getLeft(), 
				new StdAsyncCallback("SaveLatLong"){});
	}



	public void setTop(int top) {
		super.setTop(top);
		fti.setLatitudeOnIsland(top);
	}
	public TopicIdentifier getIdentifier() {		
		return fti;
	}
}
