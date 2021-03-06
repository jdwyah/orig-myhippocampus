package com.aavu.client.gui.hierarchy;

import java.util.Iterator;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AddToTopicCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.DblClickListener;
import com.aavu.client.gui.ext.FocusPanelExt;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TopicBubble extends AbstractBubbleParent implements TopicDisplayObj, DblClickListener {

	private FullTopicIdentifier fti;

	public TopicBubble(FullTopicIdentifier fti, Image image, HierarchyDisplay display) {
		super(fti.getLongitudeOnIsland(), fti.getLatitudeOnIsland(), fti.getTopicTitle(), image,
				display);

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

	public TopicIdentifier getIdentifier() {
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

	// @Override
	protected int getUnscaledHeight() {
		return 50;
	}

	// @Override
	protected int getUnscaledWidth() {
		return 50;
	}

	public void grow() {
		// TODO Auto-generated method stub

	}

	// protected boolean isDetailsShowing() {
	// return detailsShowing;
	// }

	public void onDblClick(Widget sender) {
		getDisplay().getManager().getGui().navigateTo(getFTI());
	}

	public void receivedDrop(TopicDisplayObj bubble) {

		Set selectedTopics = getDisplay().getManager().getSelectedTopics();

		// will already be there, unless they started dragging before the click event occurred.
		//
		selectedTopics.add(bubble.getTopic());

		for (Iterator iterator = selectedTopics.iterator(); iterator.hasNext();) {
			Topic name = (Topic) iterator.next();
			boolean suc = getDisplay().removeIsland(name.getId());
			System.out.println("TopicBubble. SELECTED TOPIC removed " + name + " suc: " + suc);
		}

		AddToTopicCommand command = new AddToTopicCommand(selectedTopics, getTopic(), getDisplay()
				.getCurrentRoot());

		System.out.println("TOPICBUBBLE reeive DROP command " + command);

		getDisplay().getManager().getTopicCache().executeCommand(bubble.getTopic(), command,
				new StdAsyncCallback(ConstHolder.myConstants.save()) {
					// @Override
					public void onSuccess(Object result) {
						super.onSuccess(result);
					}
				});

		// important otherwise if they do some drag dropping, then go straight to doing some more,
		// we may never do this unselect and topics will end up going places they're not meant to
		getDisplay().getManager().unselect();

	}



	// @Override
	protected void saveLocation() {
		// Set selectedTopics = getDisplay().getManager().getSelectedTopics();
		// selectedTopics.add(getTopic());
		// for (Iterator iterator = selectedTopics.iterator(); iterator.hasNext();) {
		// Topic name = (Topic) iterator.next();
		// System.out.println("TopicBubble. SELECTED TOPIC " + name + " ");
		// }
		// System.out.println("-----");


		getDisplay().getManager().getTopicCache().saveTopicLocationA(
				getDisplay().getCurrentRoot().getId(), getFTI().getTopicID(), getTop(), getLeft(),
				new StdAsyncCallback("SaveLatLong") {
				});
	}

	public void setTop(int top) {
		super.setTop(top);
		fti.setLatitudeOnIsland(top);
	}

	protected void showDetails(boolean ctrlKey) {
		// detailsShowing = true;
		getDisplay().doSelection(new RealTopic(getFTI()), ctrlKey);
		// getDisplay().getManager()
		// HoverManager.showHover(getDisplay().getManager(), this, getFTI());

	}



}
