package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.URI;
import com.aavu.client.domain.commands.SaveDatesCommand;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePickerInterface;
import com.aavu.client.widget.datepicker.HDatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * PEND MED rework and clean up duplicate code
 * 
 * @author Jeff Dwyer
 * 
 */
public class TitleGadget extends Gadget {


	private EditableLabelExtension titleBox;
	private Topic topic;
	// private StatusPicker picker;
	private DatePickerInterface datePicker;

	private Image deleteB;
	private HorizontalPanel uriPanel;
	private Image sharedB;
	private Label titleLabel;
	private Label dateLabel;
	private Label titlePromptLabel;

	// private static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");



	public TitleGadget(final Manager manager) {
		super(manager);

		titleLabel = new Label();
		titleBox = new EditableLabelExtension("", new ChangeListener() {
			public void onChange(Widget sender) {
				manager.getTopicCache().executeCommand(topic,
						new SaveTitleCommand(topic, titleBox.getText()),
						new StdAsyncCallback(ConstHolder.myConstants.save()) {

							// @Override
							public void onSuccess(Object result) {
								super.onSuccess(result);
								// manager.
							}

						});
			}
		});

		// datePicker = new DatePickerTimeline(manager,400,200,null);

		datePicker = new HDatePicker(HorizontalPanel.ALIGN_RIGHT);
		datePicker.setWeekendSelectable(true);
		datePicker.setDateFormat(DateFormatter.DATE_FORMAT_MMDDYYYY);



		datePicker.addChangeListener(new ChangeListener() {
			public void onChange(final Widget sender) {
				DatePickerInterface dp = (DatePickerInterface) sender;
				System.out.println("cur " + dp.getCurrentDate());
				// System.out.println("dp "+dp.getText());
				System.out.println("dp " + dp.getSelectedDate());

				DeferredCommand.add(new Command() {

					public void execute() {
						SimpleDatePicker dp = (SimpleDatePicker) sender;
						Date cDate = dp.getSelectedDate();

						if (!topic.getCreated().equals(cDate)) {
							System.out.println("\n\n!= " + cDate + " " + topic.getCreated());

							manager.getTopicCache().executeCommand(
									topic,
									new SaveDatesCommand(topic, cDate),
									new StdAsyncCallback(ConstHolder.myConstants.save()
											+ "TitleDate"));
						} else {
							System.out.println("\n\nEQAL forget it");
						}
					}
				});
			}
		});



		CellPanel titleP = new HorizontalPanel();
		titlePromptLabel = new Label(ConstHolder.myConstants.title());
		titleP.add(titlePromptLabel);

		if (manager.isEdittable()) {
			titleP.add(titleBox);
		} else {
			titleP.add(titleLabel);
		}

		// picker = new StatusPicker(manager);



		/**
		 * Double callback wrapping delete button
		 * 
		 * First ask show the user the repercussions of their delete, then give them a delete button
		 * to perform the operation.
		 * 
		 * TODO bringUpChart(desktop?) if curTopic = the one we're deleting
		 */
		deleteB = ConstHolder.images.bin_closed().createImage();
		deleteB.addMouseListener(new TooltipListener(ConstHolder.myConstants.delete()));
		deleteB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				manager.getTopicCache().getDeleteList(topic.getId(), new EZCallback() {
					private PopupWindow deleteWindow;

					public void onSuccess(Object result) {

						List topics = (List) result;


						VerticalPanel vp = new VerticalPanel();
						vp.add(new Label("Will delete:"));
						VerticalPanel insidePanel = new VerticalPanel();
						for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
							TopicIdentifier topic = (TopicIdentifier) iterator.next();
							insidePanel.add(new Label("Title: " + topic.getTopicTitle()));
						}
						Button confirmDeleteB = new Button("Yes, delete these (" + topics.size()
								+ ") topic(s).");
						confirmDeleteB.addClickListener(new ClickListener() {
							public void onClick(Widget sender) {



								manager.delete(topic, new StdAsyncCallback("Delete") {
									// @Override
									public void onSuccess(Object result) {
										super.onSuccess(result);
										manager.unselect();
										manager.getGui().hideCurrentHover();
										deleteWindow.close();
									}
								});
							}
						});
						ScrollPanel scrollP = new ScrollPanel(insidePanel);
						scrollP.setHeight("300px");
						vp.add(scrollP);
						vp.add(confirmDeleteB);
						deleteWindow = manager.displayInfo(vp);
					}
				});



			}
		});
		titleP.add(deleteB);


		sharedB = ConstHolder.images.shared_not().createImage();
		sharedB.addMouseListener(new TooltipListener(ConstHolder.myConstants.share()));
		sharedB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				manager.getTopicCache().getMakePublicList(topic.getId(), new EZCallback() {
					private PopupWindow editVisWindow;

					public void onSuccess(Object result) {

						final List topics = (List) result;

						String makePublicString = "private";
						final boolean newVisibility = !topic.isPublicVisible();
						if (newVisibility) {
							makePublicString = "public";
						}


						VerticalPanel vp = new VerticalPanel();
						Grid insidePanel = new Grid(topics.size(), 2);

						vp.add(new Label("Select topics to change:"));

						final Map checkBoxes = new HashMap();
						int row = 0;
						for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
							TopicIdentifier topic = (TopicIdentifier) iterator.next();

							CheckBox cb = new CheckBox("Title: " + topic.getTopicTitle());
							cb.setChecked(true);

							if (topic.isPublicVisible()) {
								insidePanel.setWidget(row, 0, ConstHolder.images.shared()
										.createImage());
							} else {
								insidePanel.setWidget(row, 0, ConstHolder.images.shared_not()
										.createImage());
							}

							insidePanel.setWidget(row, 1, cb);

							checkBoxes.put(cb, topic);
							row++;
						}

						HorizontalPanel confirmP = new HorizontalPanel();
						Label confirmMessage = new Label("Yes, make the checked topics");

						Button confirmMakePublicB = new Button("public");
						Button confirmMakePrivateB = new Button("private");

						confirmP.add(confirmMessage);
						confirmP.add(confirmMakePublicB);
						confirmP.add(confirmMakePrivateB);

						confirmMakePublicB.addClickListener(new ConfirmSharedClickListener(true,
								editVisWindow, checkBoxes));
						confirmMakePrivateB.addClickListener(new ConfirmSharedClickListener(false,
								editVisWindow, checkBoxes));

						ScrollPanel scrollP = new ScrollPanel(insidePanel);
						scrollP.setHeight("300px");
						vp.add(scrollP);
						vp.add(confirmMakePublicB);
						editVisWindow = manager.displayInfo(vp);
					}
				});
			}
		});
		titleP.add(sharedB);

		titleP.addStyleName("H-TitleGadgetOptions");



		uriPanel = new HorizontalPanel();

		dateLabel = new Label();
		CellPanel dateP = new HorizontalPanel();
		dateP.add(new Label(ConstHolder.myConstants.date()));

		if (manager.isEdittable()) {
			dateP.add(datePicker.getWidget());
		} else {
			dateP.add(dateLabel);
		}

		VerticalPanel mainP = new VerticalPanel();
		mainP.add(titleP);
		mainP.add(dateP);
		mainP.add(uriPanel);
		initWidget(mainP);
	}

	// @Override
	public Image getPickerButton() {
		throw new UnsupportedOperationException();
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.options();
	}

	/**
	 * handle clicks on "make them 'public'" or "make them 'private'" buttons
	 * 
	 * @author Jeff Dwyer
	 * 
	 */
	private class ConfirmSharedClickListener implements ClickListener {

		private boolean newVisibility;
		private Map checkBoxes;
		private PopupWindow editVisWindow;

		public ConfirmSharedClickListener(boolean newVisibility, PopupWindow editVisWindow,
				Map checkBoxes) {
			this.newVisibility = newVisibility;
			this.checkBoxes = checkBoxes;
			this.editVisWindow = editVisWindow;
		}

		public void onClick(Widget sender) {

			List tisToSend = new ArrayList();
			for (Iterator iterator2 = checkBoxes.keySet().iterator(); iterator2.hasNext();) {
				CheckBox cb = (CheckBox) iterator2.next();
				TopicIdentifier ti = (TopicIdentifier) checkBoxes.get(cb);
				if (cb.isChecked()) {
					tisToSend.add(ti);
				}
			}

			manager.getTopicCache().editVisibility(tisToSend, newVisibility,
					new StdAsyncCallback("Edit Visibility") {

						// @Override
						public void onSuccess(Object result) {
							super.onSuccess(result);
							topic.setPublicVisible(newVisibility);
							load(topic);
							editVisWindow.close();
						}
					});
		}

	}

	// @Override
	public int load(Topic topic) {
		super.load(topic);
		this.topic = topic;

		if (topic.getTitle() == null || topic.getTitle().equals("")) {
			titleBox.setText(ConstHolder.myConstants.topic_blank());
			titleLabel.setText(ConstHolder.myConstants.topic_blank());
		} else {
			titleBox.setText(topic.getTitle());
			titleLabel.setText(topic.getTitle());
		}

		// don't let them delete Root
		deleteB.setVisible(topic.isDeletable() && manager.isEdittable());
		//
		sharedB.setVisible(topic.isPublicVisibleEditable() && manager.isEdittable());

		if (topic.isPublicVisible()) {
			ConstHolder.images.shared().applyTo(sharedB);
		} else {
			ConstHolder.images.shared_not().applyTo(sharedB);
		}

		// picker.load(topic);

		datePicker.setSelectedDate(topic.getCreated());
		dateLabel.setText(datePicker.getDateFormatter().formatDate(topic.getCreated()));
		// datePicker.setCurrentDate(topic.getCreated());


		uriPanel.clear();
		if (topic instanceof URI) {
			ExternalLink urlGoB = new ExternalLink((URI) topic, null, false);
			uriPanel.add(new Label(ConstHolder.myConstants.goThere()));
			uriPanel.add(urlGoB);
		}

		titlePromptLabel.setText(topic.getTitlePromptText());

		// datePicker.setText(df.format(mv.getStartDate()));

		return 1;
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		return true;
	}

	// @Override
	public void onClick(Manager manager) {
		throw new UnsupportedOperationException();
	}

	// @Override
	public boolean isDisplayer() {
		return true;
	}

	// @Override
	public boolean showForIsCurrent(boolean isCurrent) {
		return true;
	}
}
