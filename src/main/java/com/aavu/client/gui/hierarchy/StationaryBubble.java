package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Image;

public class StationaryBubble extends AbstractBubble {

	private TopicIdentifier ti;

	public StationaryBubble(TopicIdentifier ti, Manager manager) {
		super(ti.getTopicTitle(), new Image(ImageHolder.getImgLoc("hierarchy/") + "ball_red.png"),
				manager);
		this.ti = ti;


		System.out.println("Banner " + getBanner());

	}

	// @Override
	protected void onAttach() {
		super.onAttach();
		// need this to setup dimensions
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				getBanner().setToZoom(1.0);
				System.out.println("^^^^^^^^^^");
			}
		});

	}

	// @Override
	protected void hover() {
		HoverManager.showHover(getManager(), this, ti);
	}

	// @Override
	protected void unhover() {
		HoverManager.hideHover(ti);
	}

	// @Override
	protected void saveLocation() {
		// TODO Auto-generated method stub

	}


	public void processDrag(double currentScale) {
		// TODO Auto-generated method stub

	}

	public void setLeft(int longitude) {
		// TODO Auto-generated method stub

	}

	public void setTop(int latitude) {
		// TODO Auto-generated method stub

	}

	public int getLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTop() {
		// TODO Auto-generated method stub
		return 0;
	}

}
