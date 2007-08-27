package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.service.MindscapeManager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AddTopicPopup extends PopupWindow implements CompleteListener {

	private static final int HEIGHT = 60;
	private static final int WIDTH = 290;

	private Manager manager;
	private Label messageLabel;
	private TopicCompleter suggestBox;
	private AsyncCallback after;

	/**
	 * Prevents multiple instances with a semaphore.
	 * 
	 * @param lnglat
	 * @param prototype
	 * 
	 * @param island
	 * 
	 * @param manager
	 */
	public AddTopicPopup(MindscapeManager _manager, String title, Topic parent, int[] lnglat,
			AsyncCallback after) {

		super(_manager.newFrame(), title, WIDTH, HEIGHT);
		this.manager = _manager;
		this.after = after;

		suggestBox = new TopicCompleter(manager.getTopicCache(), parent, lnglat);
		suggestBox.setCompleteListener(AddTopicPopup.this);

		setCenteredContent(new NewWidget());
	}

	private void clicked() {

		if (!suggestBox.getText().equals("")) {

			suggestBox.complete();

		} else {
			messageLabel.setText("Enter a name");
		}
	}


	public void completed(TopicIdentifier topicID) {
		after.onSuccess(topicID);
		close();
	}

	/**
	 * private class so that we can override the onload() method
	 * 
	 * @author Jeff Dwyer
	 * 
	 */
	private class NewWidget extends Composite {
		public NewWidget() {

			HorizontalPanel panel = new HorizontalPanel();


			panel.add(suggestBox);
			panel.add(new Button(ConstHolder.myConstants.island_create(), new ClickListener() {
				public void onClick(Widget sender) {
					clicked();
				}
			}));

			messageLabel = new Label("");
			panel.add(messageLabel);

			initWidget(panel);
		}

		// @Override
		protected void onLoad() {
			super.onLoad();
			suggestBox.setFocus(true);
		}
	}


}
