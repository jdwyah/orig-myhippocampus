package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.aavu.client.widget.tags.TagOrganizerView;
import com.google.gwt.user.client.ui.impl.PopupImpl;
import com.google.gwt.user.client.ui.impl.PopupImplIE6;

public class TagEditorWindow extends PopupWindow {

	public TagEditorWindow(HippoCache hippoCache) {
		super("Edit Tags");

		TagOrganizerView tov = new TagOrganizerView(hippoCache);
						
		setContentPanel(tov);
		
	}

}