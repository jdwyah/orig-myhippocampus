package com.aavu.client.gui.connectionExplorer;

import java.util.Iterator;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConnectionExplorerWidget extends SimplePanel implements RemembersPosition {

	private int left;
	private int top;

	public ConnectionExplorerWidget(Topic topic, int top, int left) {
		this.top = top;
		this.left = left;

		System.out.println("conn explorer " + topic);

		VerticalPanel mainP = new VerticalPanel();

		HorizontalPanel tags = new HorizontalPanel();

		for (Iterator iterator = topic.getTypesAsTopics().iterator(); iterator.hasNext();) {
			Topic tag = (Topic) iterator.next();
			tags.add(new TopicLink(tag));
		}
		mainP.add(tags);

		HorizontalPanel middleLevel = new HorizontalPanel();
		middleLevel.add(new TopicLink(topic));

		VerticalPanel associationP = new VerticalPanel();
		Association seeAlsoAssoc = topic.getSeeAlsoAssociation();

		for (Iterator iter = seeAlsoAssoc.getMembers().iterator(); iter.hasNext();) {
			Topic also = (Topic) iter.next();
			associationP.add(new TopicLink(also.getIdentifier()));
		}
		middleLevel.add(associationP);
		mainP.add(middleLevel);

		HorizontalPanel bottomP = new HorizontalPanel();
		bottomP.add(new Label("bottom"));


		mainP.add(bottomP);



		add(mainP);

		setPixelSize(300, 300);
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public void zoomToScale(double currentScale) {
		// TODO Auto-generated method stub

	}

	public Widget getWidget() {
		return super.getWidget();
	}

}
