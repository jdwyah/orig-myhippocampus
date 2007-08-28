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


	// private static final int zoomedBackHeight = 49 * 4;
	// private static final int zoomedBackWidth = 60 * 4;
	// private IslandBanner zoomedBackBanner;
	// private Image zoomedBackImage;
	// private FocusPanelExt zoomedBackP;
	// private SimplePanel simplePanel;

	public EntryRichText(ResizeHandler handler, HierarchyDisplay display) {
		this.display = display;

		// this.zoomedBackImage = new Image(ImageHolder.getImgLoc("hierarchy/") +
		// "entryZoomBack.png");
		// zoomedBackBanner = new IslandBanner("", 1);
		//
		//
		// AbsolutePanel zabsP = new AbsolutePanel();
		// zabsP.add(zoomedBackImage, 0, 0);
		// zabsP.add(zoomedBackBanner, 0, 0);
		// zoomedBackP = new FocusPanelExt(zabsP);

		caption = new EntryCaption(this);

		rtMainPanel = new BorderThemedPanel();
		rtMainPanel.setResizable(true);

		textArea = new SpecialTextbox(false);

		textArea.addChangeListener(caption);

		rtMainPanel.setContent(textArea);
		rtMainPanel.setCaption(caption.getDragHandle(), caption);

		rtMainPanel.setVisible(true);

		rtMainPanel.addResizeHandler(handler);

		// simplePanel = new SimplePanel();
		//
		// simplePanel.add(rtMainPanel);

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

		// zoomedBackBanner.setText(e.getTitle());

		caption.setText(e.getTitle());
		caption.setNeedsSave(false);
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

	public void setZoomedBack(boolean zoomedBack) {
		// if (zoomedBack) {
		// simplePanel.clear();
		// simplePanel.add(zoomedBackP);
		// } else {
		// simplePanel.clear();
		// simplePanel.add(rtMainPanel);
		// }
	}


}
