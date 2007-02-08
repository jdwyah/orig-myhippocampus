package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractSaveCommand;

public interface TopicSaveListener {
	void topicSaved(Topic topic, AbstractSaveCommand command);
}
