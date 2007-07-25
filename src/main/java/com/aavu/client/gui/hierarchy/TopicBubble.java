package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.DblClickListener;
import com.aavu.client.gui.ext.FocusPanelExt;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TopicBubble extends AbstractBubbleParent implements TopicDisplayObj, DblClickListener {

	private FullTopicIdentifier fti;

	private boolean detailsShowing = false;

	public TopicBubble(FullTopicIdentifier fti, HierarchyDisplay display) {
		super(fti.getLongitudeOnIsland(), fti.getLatitudeOnIsland(), fti.getTopicTitle(),
				new Image(ImageHolder.getImgLoc("hierarchy/") + "ball_white.png"), display);

		this.fti = fti;

		setDropController(new BubbleDropController(this));

		addDblClickListener(this);


	}

	public FocusPanelExt getFocusPanel() {
		return this;
	}

	public FullTopicIdentifier getFTI() {
		return fti;
	}

	/**
	 * NOTE: just wrapping the FTI. Not a fully loaded topic.
	 * 
	 * @return
	 */
	public Topic getTopic() {
		return new RealTopic(getFTI());
	}

	public void grow() {
		// TODO Auto-generated method stub

	}

	public void onDblClick(Widget sender) {
		getDisplay().navigateTo(getFTI());
	}

	public void receivedDrop(TopicDisplayObj bubble) {

		if (bubble instanceof TopicBubble) {
			TopicBubble received = (TopicBubble) bubble;
			getDisplay().removeTopicBubble(received);
			getDisplay().getManager().getTopicCache().executeCommand(
					received.getTopic(),
					new SaveTagtoTopicCommand(received.getTopic(), getTopic(), getDisplay()
							.getCurrentRoot()),
					new StdAsyncCallback(ConstHolder.myConstants.save()) {
						// @Override
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

		System.out.println("TopicBubble recievedDrop widget");

		// display.processDrop(this,received);

	}

	// @Override
	protected void saveLocation() {
		getDisplay().getManager().getTopicCache().saveTopicLocationA(
				getDisplay().getCurrentRoot().getId(), getFTI().getTopicID(), getTop(), getLeft(),
				new StdAsyncCallback("SaveLatLong") {
				});
	}

	public void setTop(int top) {
		super.setTop(top);
		fti.setLatitudeOnIsland(top);
	}

	public TopicIdentifier getIdentifier() {
		return fti;
	}

	// @Override
	protected void hover() {
		// showDetailButton();
		showDetails();
	}

	// @Override
	protected void unhover() {
		// hideDetailButton();
		hideDetails();
	}

	protected void showDetails() {
		detailsShowing = true;
		getDisplay().showHover(getFTI());
		// HoverManager.showHover(getDisplay().getManager(), this, getFTI());

	}

	protected void hideDetails() {
		detailsShowing = false;

		getDisplay().hideHoverIn1(getFTI());
		// HoverManager.hideHoverIn1(getFTI());
	}

	protected boolean isDetailsShowing() {
		return detailsShowing;
	}

	// @Override
	protected int getUnscaledHeight() {
		return 50;
	}

	// @Override
	protected int getUnscaledWidth() {
		return 50;
	}


}
