package com.aavu.client.gui.timeline.draggable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.HippoTimeline;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


public class NewHippoTimeLine extends Composite implements ChangeListener, HippoTimeline {



	private static final double DETAIL_PCT = .85;

	private static final double DETAIL_SCALE = 2.5;
	private static final int HEIGHT = 400;

	private static final int MIN_RESIZE = 300;

	private static final int WIDTH = 680;


	private Ether detailView;


	private List ethers = new ArrayList();



	private int height;
	private Manager manager;

	private Ether overView;


	public NewHippoTimeLine(Manager manager, int width, int height, CloseListener close) {

		super();

		this.manager = manager;
		this.height = height;


		detailView = new DetailsEther(manager, width, (int) (height * DETAIL_PCT));
		detailView.addChangeListener(this);

		overView = new OverViewEther(manager, width, (int) (height * (1.0 - DETAIL_PCT)));
		overView.addChangeListener(this);

		ethers.add(detailView);
		ethers.add(overView);

		// VerticalPanel put them on top of each other.
		AbsolutePanel mainPanel = new AbsolutePanel();
		mainPanel.add(detailView);
		mainPanel.add(overView, 0, (int) (height * DETAIL_PCT));
		mainPanel.setPixelSize(width, height);

		initWidget(mainPanel);

	}



	protected void add(TimeLineObj date, int depth, int key) {
		for (Iterator iterator = ethers.iterator(); iterator.hasNext();) {
			Ether eth = (Ether) iterator.next();
			eth.add(date, depth, key);
		}
	}



	public void clear() {
		detailView.clear();
		overView.clear();
		init();
	}



	private void init() {
		detailView.init();
		overView.init();
	}

	public void add(List timelines) {

		TreeOfTime tree = new TreeOfTime(1, 3, 5, detailView.getNumberOfSlots());

		for (Iterator iter = timelines.iterator(); iter.hasNext();) {
			TimeLineObj tlo = (TimeLineObj) iter.next();
			tree.add(tlo.getHasDate());
		}

		tree.visit(new Visitor() {
			public void found(Object date, int depth, int key) {
				add((TimeLineObj) date, depth, key);
			}
		});

		// System.out.println(tree.toPrettyString());

		redraw();
	}



	public void onChange(final Widget sender) {

		if (sender == overView) {

			// System.out.println("overView.getCurbackX() "+overView.getCurbackX());

			detailView.centerOn(overView.getCenterDate());

			// detailView.moveTo(overView.getLeft(),0);

		} else if (sender == detailView) {

			// System.out.println("detailView.getCurbackX() "+detailView.getCurbackX());

			overView.centerOn(detailView.getCenterDate());

			// overView.moveTo(detailView.getLeft(),0);

		}
	}



	private void redraw() {
		detailView.redraw();
		overView.redraw();
	}

	public void resize(int newWidth, int newHeight) {
		// TODO Auto-generated method stub

	}

	public Widget getWidget() {
		return this;
	}



}
