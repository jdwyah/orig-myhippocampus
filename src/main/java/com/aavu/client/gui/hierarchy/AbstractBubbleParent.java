package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractBubbleParent extends AbstractDraggableBubble {

	private static final int HIDE_DETAIL_BUTTON_IN = 800;
	private IslandBanner banner;
	private int currentHeight;
	private int currentWidth;

	private Image image;


	private AbsolutePanel mainPanel;


	private double lastScale;

	// private Button showDetailsB;
	// private FocusPanelExt showDetailsP;
	private Timer showDetailHideTimer;

	public AbstractBubbleParent(int longitudeOnIsland, int latitudeOnIsland, String topicTitle,
			Image image, HierarchyDisplay display) {
		super(longitudeOnIsland, latitudeOnIsland, topicTitle, display);
		this.image = image;

		init();
	}

	protected Widget getOurWidget() {


		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(getUnscaledWidth(), getUnscaledHeight());


		this.image.setPixelSize(getUnscaledWidth(), getUnscaledHeight());

		// System.out.println("AbstractBubble left "+left+" top "+top);

		banner = new IslandBanner(getTitle(), 5);

		mainPanel.add(image, 0, 0);
		mainPanel.add(banner, 0, 0);

		// showDetailsB = new Button("+");
		// showDetailsB.setVisible(false);
		//
		// showDetailsP = new FocusPanelExt();
		// showDetailsP.addClickListener(new ClickListener() {
		// public void onClick(Widget sender) {
		// DOM.eventCancelBubble(showDetailsP.getLastEvent(), true);
		// toggleDetails();
		// }
		// });
		//
		// // don't let a scheduled hide hide us if the mouse has entered us
		// showDetailsP.addMouseListener(new MouseListenerAdapter() {
		// public void onMouseEnter(Widget sender) {
		// showDetailHideTimer.cancel();
		// }
		// });
		// showDetailsP.add(showDetailsB);
		// mainPanel.add(showDetailsP, 0, 0);
		//
		// showDetailHideTimer = new Timer() {
		// // @Override
		// public void run() {
		// showDetailsB.setVisible(false);
		// zoomToScale(lastScale);
		// }
		// };

		return mainPanel;
	}

	protected abstract int getUnscaledWidth();

	protected abstract int getUnscaledHeight();

	public int getCurrentHeight() {
		return currentHeight;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}

	public Widget getDropTarget() {
		return image;
	}

	public void update(Topic t) {
		System.out.println("AbstractBubbleParent setting banner " + t);
		banner.setText(t.getTitle());
	}

	public IslandBanner getBanner() {
		return banner;
	}

	protected abstract void showDetails();



	// private void toggleDetails() {
	// if (isDetailsShowing()) {
	// hideDetails();
	// // showDetailsB.setText("+");
	// } else {
	// showDetails();
	// // showDetailsB.setText("-");
	// }
	// }

	protected abstract boolean isDetailsShowing();

	protected void showDetailButton() {
		// if (isDetailsShowing()) {
		// showDetailsB.setText("-");
		// } else {
		// showDetailsB.setText("+");
		// }
		// mainPanel.setWidgetPosition(showDetailsP, currentWidth, 0);
		// mainPanel.setPixelSize(currentWidth + 20, currentHeight);
		// showDetailsB.setVisible(true);
	}

	protected void hideDetailButton() {
		showDetailHideTimer.schedule(HIDE_DETAIL_BUTTON_IN);
	}

	public void zoomToScale(double currentScale) {

		lastScale = currentScale;

		image.setPixelSize((int) (getUnscaledWidth() * currentScale),
				(int) (getUnscaledHeight() * currentScale));

		Widget minimumWidget = banner.setToZoom(currentScale);

		currentWidth = (minimumWidget.getOffsetWidth() > (int) (getUnscaledWidth() * currentScale)) ? minimumWidget
				.getOffsetWidth()
				: (int) (getUnscaledWidth() * currentScale);

		currentHeight = ((int) (getUnscaledHeight() * currentScale) < minimumWidget
				.getOffsetHeight()) ? minimumWidget.getOffsetHeight()
				: (int) (getUnscaledHeight() * currentScale);

		// System.out.println("w "+correctedWidth+" h "+correctedHeight);



		mainPanel.setPixelSize(currentWidth, currentHeight);

	}
}
