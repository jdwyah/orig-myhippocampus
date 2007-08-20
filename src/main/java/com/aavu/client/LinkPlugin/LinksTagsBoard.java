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
import com.aavu.client.domain.util.CollectionUtils;
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

			for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
				Topic t = (Topic) iterator.next();
				System.out.println("t " + t + " rem " + topic + " " + t.equals(topic));
				System.out.println("HH " + t.hashCode() + " | " + topic.hashCode());
			}
			System.out.println();

			boolean r1 = CollectionUtils.removeFromCollectionById(topics, topic.getId());
			System.out.println("LTB rem " + r1 + " ");

			// NOTE
			// topics.add(topic);

		}
	}

	public void add(TopicIdentifier to, Widget w) {

		// prepend

		topics.add(new RealTopic(to));

		topicM.put(to, w);

		onPanel.add(w);
	}

	public List getAllTopics() {
		return topics;
	}

	public void clearText() {
		topicCompleter.setText("");
	}



}
