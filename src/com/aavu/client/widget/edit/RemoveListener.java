package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.ui.Widget;

public interface RemoveListener {

	void remove(Topic topic, Widget widgetToRemoveOnSuccess);

}
