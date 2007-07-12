package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReferenceBoard extends Gadget {

	private Manager manager;

	private VerticalPanel refPanel;

	public ReferenceBoard(Manager manager) {

		super(ConstHolder.myConstants.references());

		this.manager = manager;


		VerticalPanel mainP = new VerticalPanel();

		refPanel = new VerticalPanel();

		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new Label(ConstHolder.myConstants.references()));

		mainP.add(cp);
		mainP.add(refPanel);


		initWidget(mainP);

	}

	public int load(Topic topic) {

		manager.getTopicCache().getLinksTo(topic, new StdAsyncCallback("GetLinksTo") {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				List list = (List) result;

				refPanel.clear();

				if (list.size() == 0) {
					refPanel.add(new Label(ConstHolder.myConstants.references_none()));
				}

				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TopicIdentifier topicIdent = (TopicIdentifier) iter.next();
					refPanel.add(new TopicLink(topicIdent));
				}

				// updateTitle(refPanel,ConstHolder.myConstants.referencesN(list.size()));
				// bar.updateTitle(AllReferencesPanel.this,ConstHolder.myConstants.all_referencesN(totalSize
				// + list.size()));
			}

		});

		return 0;
	}

	// @Override
	public Image getPickerButton() {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public String getDisplayName() {
		return null;
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		// TODO Auto-generated method stub
		return false;
	}


	// @Override
	public void onClick(Manager manager) {
		throw new UnsupportedOperationException();
	}
}
