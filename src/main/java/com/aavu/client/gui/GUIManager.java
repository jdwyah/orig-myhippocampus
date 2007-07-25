package com.aavu.client.gui;

import com.aavu.client.domain.dto.TopicIdentifier;

public interface GUIManager {

	void showHover(TopicIdentifier ti);

	void hideCurrentHover();

	void hideHoverIn1(TopicIdentifier ti);

}
