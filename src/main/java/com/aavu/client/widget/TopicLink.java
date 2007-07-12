package com.aavu.client.widget;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.gui.timeline.CloseListener;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicLink extends SimplePanel implements ClickListener {

	private static final String HOVER_STYLE = "H-TopicLink-hover";
	private static final int DEFAULT_MAX_STRING = 40;
	private Label l;
	protected long id;
	private CloseListener popup;

	/**
	 * dummyLink
	 * 
	 */
	public TopicLink() {
		this("", 0);
	}

	public TopicLink(final Topic to) {
		this(to.getTitle(), to.getId());
	}

	public TopicLink(TopicIdentifier topic) {
		this(topic.getTopicTitle(), topic.getTopicID());
	}

	public TopicLink(TopicIdentifier topic, CloseListener popup) {
		this(topic.getTopicTitle(), topic.getTopicID(), DEFAULT_MAX_STRING, popup);
	}

	private TopicLink(String title, final long id) {
		this(title, id, DEFAULT_MAX_STRING, null);
	}

	public TopicLink(TopicIdentifier topic, CloseListener popup, int maxStringLength) {
		this(topic.getTopicTitle(), topic.getTopicID(), maxStringLength, popup);
	}

	public TopicLink(String title, long id, CloseListener popup) {
		this(title, id, DEFAULT_MAX_STRING, popup);
	}

	public TopicLink(String title, long id, int maxStringLength, CloseListener popup) {
		this.popup = popup;

		l = null;
		if (title.length() > maxStringLength) {
			l = new Label(title.substring(0, maxStringLength - 3) + "...", false);
			l.addMouseListener(new TooltipListener(0, 20, title));
		} else {
			l = new Label(title, false);
		}
		this.id = id;
		l.addClickListener(this);

		l.addMouseListener(new MouseListenerAdapter() {
			public void onMouseEnter(Widget sender) {
				l.addStyleName(HOVER_STYLE);
			}

			public void onMouseLeave(Widget sender) {
				l.removeStyleName(HOVER_STYLE);
			}
		});

		l.setStyleName("H-TopicLink");

		add(l);

		sinkEvents(Event.ONCLICK);

	}

	public void load(TopicIdentifier to) {
		l.setText(to.getTopicTitle());
		id = to.getTopicID();
	}

	public void onClick(Widget sender) {
		System.out.println("TopicLink.onClick");
		History.newItem(id + "");
	}

	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		System.out.println("TopicLink.onBrowserEvent sadfsdfsdf");
		if (popup != null) {
			if (DOM.eventGetType(event) == Event.ONCLICK) {
				if (!DOM.eventGetCtrlKey(event)) {
					popup.close();
				}
			}
		}

	}

	public void addMouseListener(MouseListener listener) {
		l.addMouseListener(listener);
	}


}
