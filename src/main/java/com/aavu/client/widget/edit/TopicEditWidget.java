package com.aavu.client.widget.edit;

import com.aavu.client.domain.Entry;
import com.aavu.client.gui.EntryEditWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicEditWidget extends Composite implements SourcesChangeEvents {

	private SpecialTextbox textArea = null;

	// private TagBoard tagBoard;
	// private SubjectBoard subjectBoard;

	private Entry entry;


	private Manager manager;

	public TopicEditWidget(final Manager manager, Entry entry) {
		this.entry = entry;

		this.manager = manager;

		System.out.println("topic edit widg " + entry);

		textArea = new SpecialTextbox(true);
		textArea.setSize((EntryEditWindow.WIDTH - 120) + "px", "400px");

		// tagBoard = new TagBoard(manager);
		// subjectBoard = new SubjectBoard(manager,titleBox,tagBoard);


		VerticalPanel panel = new VerticalPanel();

		// panel.add(subjectBoard);
		// panel.add(tagBoard);

		panel.add(textArea);


		initWidget(panel);


	}


	// @Override
	protected void onAttach() {
		super.onAttach();
		setupTopic();
	}



	private void setupTopic() {
		System.out.println("setupTopic");
		if (entry != null) {
			textArea.setText(entry.getData());
		}
	}


	public String getCurrentText() {
		return textArea.getText();
	}



	public void addChangeListener(ChangeListener listener) {
		textArea.addChangeListener(listener);
	}



	public void removeChangeListener(ChangeListener listener) {
		textArea.removeChangeListener(listener);
	}


}
