package com.aavu.client.gui;

import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EntryEditWindow extends PopupWindow implements SaveNeededListener {

	public static final int WIDTH = 750;
	private static final int HEIGHT = 500;
	private Manager manager;
	private SaveStopLight saveButton;
	private TopicViewAndEditWidget topicViewAndEditW;
	private HorizontalPanel mainP;

	public EntryEditWindow(Entry entry, Manager manager, GInternalFrame frame) {
		super(frame, entry.getTitle(), WIDTH, HEIGHT);
		this.manager = manager;

		topicViewAndEditW = new TopicViewAndEditWidget(manager, this);
		topicViewAndEditW.setPixelSize(WIDTH - 50, HEIGHT - 40);

		saveButton = new SaveStopLight(new ClickListener() {
			public void onClick(Widget sender) {
				save();
			}
		});
		mainP = new HorizontalPanel();

		mainP.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);

		mainP.add(topicViewAndEditW);
		mainP.add(saveButton);

		setContent(mainP);

		topicViewAndEditW.load(entry);


		frame.setDefaultCloseOperation(GFrame.DO_NOTHING_ON_CLOSE);

		frame.addFrameListener(new GFrameAdapter() {
			public void frameClosing(GFrameEvent evt) {
				if (saveButton.isSaveNeeded()) {

					// DefaultGDialog.showConfirmDialog(mainP,
					// Manager.myConstants.close_without_saving(), "",
					// GDialog.OK_CANCEL_OPTION_TYPE, new GDialogChoiceListener(){
					// public void onChoice(DefaultGDialog dialog) {
					// if (dialog.getSelectedOption() == DefaultGDialog.OK_OPTION) {
					// frame.close();
					// }
					// }});

					if (Window.confirm(ConstHolder.myConstants.close_without_saving())) {
						getFrame().close();
					}

				} else {
					getFrame().close();
				}
			}
		});
	}

	public GFrame getFrame() {
		return frame;
	}

	private void save() {
		manager.getTopicCache().executeCommand(topicViewAndEditW.getEntry(),
				topicViewAndEditW.getSaveCommand(), new StdAsyncCallback("") {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						saveButton.saveAccomplished();
						frame.setCaption(topicViewAndEditW.getEntry().getTitle());
					}
				});
	}

	public void onChange(Widget sender) {
		saveButton.setSaveNeeded();
	}

}
