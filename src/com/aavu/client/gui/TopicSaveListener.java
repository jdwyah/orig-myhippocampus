package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;

public interface TopicSaveListener {
	void topicSaved(Topic topic, AbstractCommand command);
}
