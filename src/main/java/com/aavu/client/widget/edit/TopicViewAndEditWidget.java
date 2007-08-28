package com.aavu.client.widget.edit;


import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveOccurrenceDataCommand;
import com.aavu.client.gui.EntryEditWindow;
import com.aavu.client.gui.SaveStopLight;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicViewAndEditWidget extends Composite implements ChangeListener, SaveNeededListener {


	private TopicEditWidget topicEditWidget;

	private VerticalPanel topicPanel;

	public Entry entry;
	private Manager manager;

	private int width;

	private int height;

	private SaveStopLight saveButton;

	private EntryEditWindow entryEditWindow;

	public TopicViewAndEditWidget(Manager manager, EntryEditWindow entryEditWindow) {

		this.manager = manager;
		this.entryEditWindow = entryEditWindow;


		HorizontalPanel mainPanel = new HorizontalPanel();

		topicPanel = new VerticalPanel();

		saveButton = new SaveStopLight(new ClickListener() {
			public void onClick(Widget sender) {
				save();
			}
		});

		mainPanel.add(topicPanel);


		initWidget(mainPanel);
		setStyleName("H-ViewEdit");
	}


	// @Override
	public void setPixelSize(int width, int height) {
		super.setPixelSize(width, height);
		this.width = width;
		this.height = height;

	}


	public void load(Entry entry) {
		this.entry = entry;
		topicPanel.clear();

		topicEditWidget = new TopicEditWidget(manager, entry, saveButton);
		topicEditWidget.addChangeListener(this);
		topicEditWidget.setPixelSize(width, height);

		topicPanel.add(topicEditWidget);

		System.out.println("TopicViewAndEdit load title: " + entry.getTitle());


	}


	public Entry getEntry() {
		return entry;
	}

	public String getEntryText() {
		return getEditEntryText();
	}

	private String getEditEntryText() {
		return topicEditWidget.getCurrentText();
	}



	public AbstractCommand getSaveCommand() {
		return new SaveOccurrenceDataCommand(entry, entry.getTitle(), getEntryText());
	}

	private void save() {
		manager.getTopicCache().executeCommand(getEntry(), getSaveCommand(),
				new StdAsyncCallback("") {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						saveButton.saveAccomplished();
						entryEditWindow.setCaption(getEntry().getTitle());
					}
				});
	}

	public void onChange(Widget sender) {
		setSaveNeeded();
	}

	public void setSaveNeeded() {
		saveButton.setSaveNeeded();
	}

	public boolean isSaveNeeded() {
		return saveButton.isSaveNeeded();
	}

}
