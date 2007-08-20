package com.aavu.client.gui;

import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.domain.Entry;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class EntryEditWindow extends PopupWindow {

	public static final int WIDTH = 750;
	private static final int HEIGHT = 500;
	private Manager manager;

	private TopicViewAndEditWidget topicViewAndEditW;
	private HorizontalPanel mainP;

	public EntryEditWindow(Entry entry, Manager manager, GInternalFrame frame) {
		super(frame, entry.getTitle(), WIDTH, HEIGHT);
		this.manager = manager;

		topicViewAndEditW = new TopicViewAndEditWidget(manager, this);
		topicViewAndEditW.setPixelSize(WIDTH - 30, HEIGHT - 40);


		mainP = new HorizontalPanel();

		mainP.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);

		mainP.add(topicViewAndEditW);


		setContent(mainP);

		topicViewAndEditW.load(entry);


		frame.setDefaultCloseOperation(GFrame.DO_NOTHING_ON_CLOSE);

		frame.addFrameListener(new GFrameAdapter() {
			public void frameClosing(GFrameEvent evt) {
				if (topicViewAndEditW.isSaveNeeded()) {

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



}
