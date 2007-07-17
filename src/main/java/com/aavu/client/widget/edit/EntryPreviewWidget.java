package com.aavu.client.widget.edit;

import com.aavu.client.domain.Entry;
import com.aavu.client.util.Logger;
import com.aavu.client.wiki.TextDisplay;

public class EntryPreviewWidget extends TopicWidget {

	private static final int NUM_CHARS = 240;


	// @Override
	public void setText(Entry entry) {

		textPanel.clear();

		Logger.log("entry " + entry.getData());
		Logger.log("chopBody " + entry.getDataWithoutBodyTags());
		String stripped = entry.getDataWithoutBodyTags();

		// TODO make sure we don't cut off in the middle of an HTML tag
		if (stripped != null && stripped.length() > NUM_CHARS) {
			String str = stripped.substring(0, NUM_CHARS);
			str += "</p></div>";
			Logger.log("CUT |" + str + "|");
			textPanel.add(new TextDisplay(str));
		} else {
			Logger.log("NO CUT |" + stripped + "|");
			textPanel.add(new TextDisplay(stripped));
		}

		System.out.println("textPanel " + textPanel);
	}

}
