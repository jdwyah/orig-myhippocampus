package com.aavu.client.gui.hierarchy;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Widget;

public class HoverManager {

	static Map hovers = new HashMap();

	public static void showHover(final Manager m, final Widget w, final TopicIdentifier ti) {

		TopicDisplayOverlay overlay = (TopicDisplayOverlay) hovers.get(ti);

		if (overlay == null) {
			m.getTopicCache().getTopic(ti, new StdAsyncCallback("Get Display Overlay") {
				// @Override
				public void onSuccess(Object result) {
					super.onSuccess(result);

					TopicDisplayOverlay overlay = new TopicDisplayOverlay((Topic) result, w, m);

					hovers.put(ti, overlay);

					showOverlay(overlay, w);
				}
			});

		} else {
			showOverlay(overlay, w);
		}
	}

	private static void showOverlay(TopicDisplayOverlay overlay, Widget w) {
		overlay.setPopupPosition(w.getAbsoluteLeft(), w.getAbsoluteTop());
		overlay.show();
	}

	public static void hideHover(TopicIdentifier ti) {

		TopicDisplayOverlay overlay = (TopicDisplayOverlay) hovers.get(ti);

		if (overlay != null) {
			overlay.hideIn1();
		}
	}

}
