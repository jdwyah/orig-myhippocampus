package com.aavu.client.gui.hierarchy;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class HoverManager {

	static Map hovers = new HashMap();
	private static TopicDisplayOverlay currentOverlay;

	public static void showHover(final Manager m, final Widget w, final TopicIdentifier ti) {
		showHover(m, w, ti, false);
	}

	public static void showHover(final Manager m, final Widget w, final TopicIdentifier ti,
			final boolean childrenOnly) {


		TopicDisplayOverlay overlay = (TopicDisplayOverlay) hovers.get(ti);


		if (overlay == null) {
			m.getTopicCache().getTopic(ti, new StdAsyncCallback("Get Display Overlay") {
				// @Override
				public void onSuccess(Object result) {
					super.onSuccess(result);

					TopicDisplayOverlay overlay = new TopicDisplayOverlay((Topic) result, w, m,
							childrenOnly);

					// Make the topic bubble activate & inactivate the overlay
					//
					if (w instanceof SourcesMouseEvents) {
						SourcesMouseEvents sourcesEvents = (SourcesMouseEvents) w;
						sourcesEvents.addMouseListener(overlay);
					}

					hovers.put(ti, overlay);

					showOverlay(overlay, w);
				}
			});

		} else {
			showOverlay(overlay, w);
		}
	}

	private static void showOverlay(TopicDisplayOverlay overlay, Widget w) {

		// PopupWindow w = new PopupWindow();
		// DefaultGFrame f = new DefaultGFrame("btp frame");
		// f.setTheme("alphacube");
		//
		// BorderThemedPanel b = new BorderThemedPanel();
		// b.setCaption("btp");
		// b.setContent(new Label("lab"));
		//
		// f.setContent(b);
		// f.setVisible(true);
		// GwmUtilities.diplayAtScreenCenter(f);


		// If we're going to switch, hide the current one first
		// so that there aren't multiple overlays visible at 1 time
		if (currentOverlay != null && overlay != currentOverlay) {
			currentOverlay.hideImmediate();
		}
		currentOverlay = overlay;



		overlay.show();

	}

	public static void hideHoverIn1(TopicIdentifier ti) {

		TopicDisplayOverlay overlay = (TopicDisplayOverlay) hovers.get(ti);

		if (overlay != null) {
			overlay.hideIn1();
		}
	}

	public static void hideHover(TopicIdentifier ti) {

		TopicDisplayOverlay overlay = (TopicDisplayOverlay) hovers.get(ti);

		if (overlay != null) {
			overlay.hideImmediate();
		}
	}

}
