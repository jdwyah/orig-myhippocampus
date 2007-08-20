package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.commands.SaveOccurrenceDataCommand;
import com.aavu.client.widget.edit.SpecialTextbox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EntryRichText extends Composite {

	private String text;

	private SpecialTextbox textArea;

	private BorderThemedPanel rtMainPanel;

	private EntryCaption caption;

	private HierarchyDisplay display;

	private Entry entry;


	public EntryRichText(ResizeHandler handler, HierarchyDisplay display) {
		this.display = display;
		caption = new EntryCaption(this);

		rtMainPanel = new BorderThemedPanel();
		rtMainPanel.setResizable(true);

		textArea = new SpecialTextbox(false);

		textArea.addChangeListener(caption);

		rtMainPanel.setContent(textArea);
		rtMainPanel.setCaption(caption.getDragHandle(), caption);

		rtMainPanel.setVisible(true);

		rtMainPanel.addResizeHandler(handler);
		initWidget(rtMainPanel);
	}

	public void setTextSize(double font_size) {
		caption.setTextSize(font_size);
	}

	public Widget getDragHandle() {

		// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!\ngetting drag handle "
		// + rtMainPanel.getDragHandle());
		//
		// System.out.println("is focus panel "
		// + (rtMainPanel.getDragHandle() instanceof FocusPanel));

		return rtMainPanel.getDragHandle();
	}

	public void load(Entry e) {
		this.entry = e;

		caption.setText(e.getTitle());

		textArea.setText(e.getData());
	}

	public String getText() {
		return textArea.getText();
	}

	// @Override
	public void setPixelSize(int width, int height) {
		super.setPixelSize(width, height);
		textArea.setPixelSize(width - 20, height - 30);
	}

	public void onChange(Widget sender) {

	}

	public SaveOccurrenceDataCommand getSaveComand() {
		return new SaveOccurrenceDataCommand(entry, caption.getText(), textArea.getText());
	}

	public Entry getEntry() {
		return entry;
	}

	public HierarchyDisplay getDisplay() {
		return display;
	}


}
