package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.commands.SaveOccurrenceDataCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.util.Logger;
import com.aavu.client.widget.edit.SpecialTextbox;
import com.allen_sauer.gwt.dragdrop.client.HasDragHandle;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class EntryDisplay extends AbstractDraggableBubble implements TopicDisplayObj,
		HasDragHandle, ResizeHandler {

	private static final double MIN_FONT = .4;

	private static final double MAX_FONT = .8;

	private static final int DEF_WIDTH = 0;

	private static final int DEF_HEIGHT = 0;

	private TopicOccurrenceConnector owl;
	private HierarchyDisplay display;

	private int unscaledHeight;
	private int unscaledWidth;
	private EntryRichText entryPreview;
	private AbsolutePanel mainPanel;

	private double lastScale;

	private Entry entry;

	public EntryDisplay(TopicOccurrenceConnector topicOccurrenceConnector,
			HierarchyDisplay hierarchyDisplay) {

		super(topicOccurrenceConnector.getLongitude(), topicOccurrenceConnector.getLatitude(),
				topicOccurrenceConnector.getOccurrence().getTitle(), hierarchyDisplay);

		this.entry = (Entry) topicOccurrenceConnector.getOccurrence();
		this.owl = topicOccurrenceConnector;
		this.display = hierarchyDisplay;


		init();

		setPixelSize(unscaledWidth, unscaledHeight);

		setStyleName("H-EntryDisplay");
		// addStyleName("H-BlueFade");


	}

	// public void onClick(Widget sender) {
	// System.out.println("EntryDisplay onClick");
	// display.getManager().editOccurrence(owl.getOccurrence());
	// }


	public DropController getDropController() {
		// TODO Auto-generated method stub
		return null;
	}

	public Widget getDropTarget() {
		return this;
	}


	public TopicIdentifier getIdentifier() {
		return owl.getOccurrence().getIdentifier();
	}

	public String getTitle() {
		return owl.getOccurrence().getTitle();
	}



	/**
	 * TODO PEND HIGH dupe of code in AbstractDraggableBubble
	 */
	protected void saveLocation() {
		getDisplay().getManager().getTopicCache().saveOccLocationA(
				getDisplay().getCurrentRoot().getId(), owl.getOccurrence().getId(), getTop(),
				getLeft(), new StdAsyncCallback("Save Occurrence LatLong") {
				});
	}



	public void receivedDrop(TopicDisplayObj bubble) {
		// TODO Auto-generated method stub

	}


	public void update(Topic t) {
		if (t instanceof Entry) {
			entry = (Entry) t;

			entryPreview.load(entry);
		} else {
			Logger.error("EntryDisplay. Update with non-entry");
		}
	}

	public void zoomToScale(double currentScale) {

		lastScale = currentScale;

		// our div
		setPixelSize((int) (unscaledWidth * currentScale), (int) (unscaledHeight * currentScale));

		// cropper. we need an absolute panel, if we're to crop.
		mainPanel.setPixelSize((int) (unscaledWidth * currentScale),
				(int) (unscaledHeight * currentScale));

		// inner widget
		entryPreview.setPixelSize((int) (unscaledWidth * currentScale),
				(int) (unscaledHeight * currentScale));


		double font_size = getFontFor(1, currentScale);

		entryPreview.setTextSize(font_size);


	}

	public Entry getEntry() {
		return entry;
	}

	public double getFontFor(int size, double zoom) {

		double s = MIN_FONT + .4 * zoom;

		s = s > MAX_FONT ? MAX_FONT : s;

		return s;
	}

	/**
	 * only drag with the top row so that the resize still works
	 */
	// @Override
	public Widget getDragHandle() {
		return entryPreview.getDragHandle();
	}


	// @Override
	protected Widget getOurWidget() {
		entryPreview = new EntryRichText(this);
		Entry e = (Entry) owl.getOccurrence();
		entryPreview.load(e);

		unscaledWidth = e.getWidth();
		unscaledHeight = e.getHeight();


		mainPanel = new AbsolutePanel();

		mainPanel.add(entryPreview);

		return mainPanel;
	}

	public void resize(int width, int height) {
		unscaledHeight = (int) (height / lastScale);
		unscaledWidth = (int) (width / lastScale);

		zoomToScale(lastScale);
	}


	// @Override
	protected void clickAction() {
		// TODO Auto-generated method stub

	}

	// @Override
	protected void unClickAction() {
		// TODO Auto-generated method stub

	}

	private class EntryRichText extends Composite {

		private String text;

		private SpecialTextbox textArea;

		private BorderThemedPanel rtMainPanel;

		private EntryCaption caption;


		public EntryRichText(ResizeHandler handler) {

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

		private SaveOccurrenceDataCommand getSaveComand() {
			return new SaveOccurrenceDataCommand(getEntry(), caption.getText(), textArea.getText());
		}



	}


	private class EntryCaption extends Composite implements SourcesMouseEvents, ChangeListener {

		private Label titleL;
		private Label saveL;
		private EntryRichText entryRichText;
		private FocusPanel fp;
		private Label richL;


		public EntryCaption(EntryRichText entryRichText) {
			this.entryRichText = entryRichText;
			HorizontalPanel mainP = new HorizontalPanel();


			titleL = new Label();
			mainP.add(titleL);

			richL = new Label("(edit)");
			richL.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					display.getManager().editOccurrence(getEntry());
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
			display.getManager().getTopicCache().executeCommand(getEntry(),
					entryRichText.getSaveComand(), new StdAsyncCallback("") {
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


}