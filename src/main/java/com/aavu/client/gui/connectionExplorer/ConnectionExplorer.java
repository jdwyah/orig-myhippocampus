package com.aavu.client.gui.connectionExplorer;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.service.Manager;

public class ConnectionExplorer extends ViewPanel {

	private Topic myTopic;
	private Manager manager;
	private int width;
	private int height;


	/**
	 * 
	 * 
	 * @param manager
	 */
	public ConnectionExplorer(Manager manager) {
		super();
		this.manager = manager;


		addStyleName("H-ConnectionExplorer");

	}

	public int load(Topic topic) {
		myTopic = topic;


		ConnectionExplorerWidget focus = new ConnectionExplorerWidget(topic, 0, 0);

		addObject(focus);

		manager.getTopicCache().getLinksTo(topic, new StdAsyncCallback("GetLinksTo") {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				List list = (List) result;


				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TopicIdentifier topicIdent = (TopicIdentifier) iter.next();
					// refPanel.add(new TopicLink(topicIdent));
				}

			}

		});



		return 1;

	}



	// @Override
	protected int getHeight() {
		return height;
	}

	// @Override
	protected int getWidth() {
		return width;
	}

	// @Override
	protected void setBackground(double scale) {
		// TODO Auto-generated method stub

	}

	public void resize(int newWidth, int newHeight) {

		width = newWidth;
		height = newHeight;



		setPixelSize(width, height);

		// drawHUD();

		redraw();

	}
}
