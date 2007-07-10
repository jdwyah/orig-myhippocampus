package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveMetaTopicCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.EnterInfoButton;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopicEditWidget extends Composite implements CompleteListener {

	private Topic topic;
	private MetaTopic meta;
	private TopicCompleter completer;
	private ActionableTopicLabel topicDisplayLink;
	private EnterInfoButton enterB;
	private TopicCache topicCache;

	public MetaTopicEditWidget(final MetaTopic meta, final Topic topic, TopicCache topicCache) {

		HorizontalPanel widget = new HorizontalPanel();

		this.topic = topic;
		this.meta = meta;
		this.topicCache = topicCache;

		completer = new TopicCompleter(topicCache);
		completer.setCompleteListener(this);

		enterB = new EnterInfoButton();
		enterB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				completer.complete();
			}
		});

		topicDisplayLink = new ActionableTopicLabel(ConstHolder.myConstants.editMe(),
				new ClickListener() {
					public void onClick(Widget sender) {
						setToEditMode();
					}
				});

		widget.add(new Label(meta.getName()));
		widget.add(completer);
		widget.add(topicDisplayLink);
		widget.add(enterB);

		Topic mv = (Topic) topic.getSingleMetaValueFor(meta);
		if (mv != null) {
			completer.setText(mv.getTitle());
			setToShowMode(mv.getIdentifier());
		}

		initWidget(widget);
	}

	private void setToEditMode() {
		completer.setVisible(true);
		topicDisplayLink.setVisible(false);
		enterB.setVisible(true);
	}

	private void setToShowMode(TopicIdentifier to) {

		topicDisplayLink.setTopicIdent(to);

		topicDisplayLink.setVisible(true);
		completer.setVisible(false);
		enterB.setVisible(false);
	}

	/**
	 * replaces the saveNowEvent
	 * 
	 * Could replace this double Async with a single if we return the to of the created.
	 * 
	 */
	public void completed(final TopicIdentifier topicID) {

		setToShowMode(topicID);

		topicCache.executeCommand(topic, new SaveMetaTopicCommand(topic, meta, new Topic(topicID)),
				new StdAsyncCallback(ConstHolder.myConstants.save()) {
				});
	}
}
