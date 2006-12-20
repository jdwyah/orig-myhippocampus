package com.aavu.client.widget.edit;

import com.aavu.client.domain.Tag;
import com.google.gwt.user.client.ui.Widget;

public interface RemoveListener {

	void remove(Tag tag, Widget widgetToRemoveOnSuccess);

}
