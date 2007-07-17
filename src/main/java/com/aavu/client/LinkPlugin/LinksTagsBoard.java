package com.aavu.client.LinkPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.util.Logger;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.DeletableTopicLabel;
import com.aavu.client.widget.edit.RemoveListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinksTagsBoard extends Composite implements RemoveListener {

	private VerticalPanel mainPanel;
	private Map topicM;
	private List topics;
	private HorizontalPanel onPanel;
	private TopicCompleter topicCompleter;
	private int removeNumber = 0;

	public LinksTagsBoard(final CompleteListener completer, TopicCache topicCache) {

		topicM = new HashMap();
		topics = new ArrayList();

		onPanel = new HorizontalPanel();
		mainPanel = new VerticalPanel();

		topicCompleter = new TopicCompleter(topicCache);
		topicCompleter.setCompleteListener(completer);

		EnterInfoButton enterInfoButton = new EnterInfoButton();
		enterInfoButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				topicCompleter.complete();
			}
		});

		mainPanel.add(onPanel);

		HorizontalPanel tp = new HorizontalPanel();
		tp.add(topicCompleter);
		tp.add(enterInfoButton);
		mainPanel.add(tp);

		initWidget(mainPanel);
	}

	public void load(WebLink w) {
		removeNumber = 0;

		topicM.clear();
		topics.clear();

		if (w != null) {
			Logger.log("found " + w.getTopics().size() + " Tags for this link");
			for (Iterator iter = w.getTopics().iterator(); iter.hasNext();) {
				TopicOccurrenceConnector t = (TopicOccurrenceConnector) iter.next();

				DeletableTopicLabel tagLabel = new DeletableTopicLabel(t.getTopic(), this);

				add(t.getTopic().getIdentifier(), tagLabel);

			}
		}
	}

	public void remove(Topic topic, Widget widgetToRemoveOnSuccess) {
		Object o = topicM.get(topic.getIdentifier());

		if (o != null) {
			DeletableTopicLabel label = (DeletableTopicLabel) o;
			onPanel.remove(label);
			topicM.remove(topic);

			// append
			topics.remove(topic);
			topics.add(topic);
			removeNumber++;
		}
	}

	public void add(TopicIdentifier to, Widget w) {

		// prepend
		if (topics.size() > 0) {
			topics.add(new RealTopic(to));
		} else {
			topics.add(0, new RealTopic(to));
		}
		topicM.put(to, w);

		onPanel.add(w);
	}

	public List getAllTopics() {
		return topics;
	}

	public void clearText() {
		topicCompleter.setText("");
	}

	public int getRemoveNumber() {
		if (removeNumber == 0) {
			return -1;
		}
		return topics.size() - removeNumber;
	}

}
