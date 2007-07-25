package com.aavu.client.gui.gadgets;

import java.util.Iterator;

import com.aavu.client.Interactive;
import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.UploadWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadBoard extends Gadget {

	private VerticalPanel mainPanel = new VerticalPanel();

	private int size = 0;

	// private SaveNeededListener saveNeeded;

	public UploadBoard(final Manager manager) {

		super(manager);

		initWidget(mainPanel);

	}


	// @Override
	public boolean isDisplayer() {
		return true;
	}


	public int load(final Topic topic) {

		mainPanel.clear();

		HorizontalPanel rowOne = new HorizontalPanel();

		Button addOne = new Button(ConstHolder.myConstants.upload_add());
		addOne.addClickListener(new ClickListener() {


			public void onClick(Widget sender) {
				UploadWidget widg = new UploadWidget(manager, topic, UploadBoard.this, Interactive
						.getRelativeURL(ConstHolder.UPLOAD_PATH));
				mainPanel.add(widg);
			}
		});
		rowOne.add(addOne);
		mainPanel.add(rowOne);

		// don't let them upload to an unsaved topic
		// TODO make this appear after a save and give an indication that they need
		// to save to make this appear.
		if (topic.getId() == 0) {
			mainPanel.clear();
			mainPanel.add(new Label(ConstHolder.myConstants.upload_save_topic_first()));
			return 0;
		}

		size = 0;
		for (Iterator iter = topic.getFiles().iterator(); iter.hasNext();) {
			S3File file = (S3File) iter.next();
			addS3File(file);
			size++;

		}
		return size;
	}

	public void addS3File(S3File file) {

		ExternalLink link = new ExternalLink(file);
		link.addStyleName("H-FileLink");
		mainPanel.add(link);
	}

	public int getSize() {
		return size;
	}

	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.gadgetFiles().createImage();
		b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
		return b;
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.files();
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasFiles();
	}

	// @Override
	public boolean enabled(User user) {
		return user.isPremiumAccount();
	}

	// @Override
	public void onClick(Manager manager) {
		manager.createNew(new S3File());
	}


}
