package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class EntryCaption extends Composite implements SourcesMouseEvents, ChangeListener {

	private Label titleL;
	private Label saveL;
	private EntryRichText entryRichText;
	private FocusPanel fp;
	private Label richL;


	public EntryCaption(final EntryRichText entryRichText) {
		this.entryRichText = entryRichText;
		HorizontalPanel mainP = new HorizontalPanel();


		titleL = new Label();
		mainP.add(titleL);

		richL = new Label("(edit)");
		richL.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				entryRichText.getDisplay().getManager().editOccurrence(entryRichText.getEntry());
			}
		});



		saveL = new Label("Save");
		saveL.addStyleName("H-SaveLabel");
		saveL.setVisible(false);
		saveL.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				save();
			}
		});
		mainP.add(saveL);

		mainP.add(richL);


		// wrap so we get drag events
		fp = new FocusPanel(mainP);
		initWidget(fp);
	}

	public void setTextSize(double font_size) {
		DOM.setStyleAttribute(richL.getElement(), "fontSize", font_size + "em");
		DOM.setStyleAttribute(titleL.getElement(), "fontSize", font_size + "em");
		DOM.setStyleAttribute(saveL.getElement(), "fontSize", font_size + "em");
	}

	public Widget getDragHandle() {
		return fp;
	}

	public String getText() {
		return titleL.getText();
	}

	public void setText(String title) {
		titleL.setText(title);
	}

	private void save() {
		entryRichText.getDisplay().getManager().getTopicCache().executeCommand(
				entryRichText.getEntry(), entryRichText.getSaveComand(), new StdAsyncCallback("") {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						saveL.setVisible(false);
					}
				});

	}

	public void addMouseListener(MouseListener listener) {
		fp.addMouseListener(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		fp.removeMouseListener(listener);
	}

	public void onChange(Widget sender) {
		saveL.setVisible(true);
	}

}
