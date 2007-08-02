package com.aavu.client.widget.edit;


import com.aavu.client.domain.Entry;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveOccurrenceDataCommand;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicViewAndEditWidget extends Composite implements ChangeListener {


	private TopicEditWidget topicEditWidget;

	private VerticalPanel topicPanel;

	public Entry entry;
	private Manager manager;
	private SaveNeededListener saveNeeded;

	private EditableLabelExtension titleBox;


	public TopicViewAndEditWidget(Manager manager, SaveNeededListener saveNeeded) {
		this.saveNeeded = saveNeeded;
		this.manager = manager;



		HorizontalPanel mainPanel = new HorizontalPanel();

		topicPanel = new VerticalPanel();


		mainPanel.add(topicPanel);


		initWidget(mainPanel);
		setStyleName("H-ViewEdit");
	}


	public void load(Entry entry) {
		this.entry = entry;
		topicPanel.clear();



		HorizontalPanel titleP = new HorizontalPanel();


		titleBox = new EditableLabelExtension(entry.getTitle(), this);

		titleP.add(new HeaderLabel(ConstHolder.myConstants.title()));
		titleP.add(titleBox);
		topicPanel.add(titleP);



		topicEditWidget = new TopicEditWidget(this, manager, entry);
		topicEditWidget.addChangeListener(this);


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


	public void onChange(Widget sender) {
		saveNeeded.onChange(this);
	}


	public AbstractCommand getSaveCommand() {
		return new SaveOccurrenceDataCommand(entry, titleBox.getText(), getEntryText());
	}


}
