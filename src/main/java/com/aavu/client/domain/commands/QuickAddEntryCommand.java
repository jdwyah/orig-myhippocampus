package com.aavu.client.domain.commands;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;

public class QuickAddEntryCommand extends SaveOccurrenceCommand {

	public QuickAddEntryCommand() {
	};

	public QuickAddEntryCommand(String title, String entryString, Topic topic) {
		Entry e = new Entry();
		e.setTitle(title);
		e.setData(entryString);
		this.occurrence = e;

		List topicsToLoad = new ArrayList();
		topicsToLoad.add(e);
		topicsToLoad.add(topic);

		setTopicIDsFromTopics(topicsToLoad);
	}

}
