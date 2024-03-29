package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.commands.SaveMetaDateCommand;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePicker;
import com.aavu.client.widget.datepicker.HDatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaDate extends Meta implements Serializable {

	private static final String TYPE = "Date";// BAD Manager.myConstants.meta_date();

	private transient HippoDate mv;

	private transient static TopicCache topicService;

	public static void setTopicService(TopicCache topicService) {
		MetaDate.topicService = topicService;
	}


	// @Override
	public String getType() {
		return TYPE;
	}

	// @Override
	public Widget getEditorWidget(final Topic topic, Manager manager) {

		HorizontalPanel widget = new HorizontalPanel();


		DatePicker datePicker = new HDatePicker(HorizontalPanel.ALIGN_RIGHT);

		// Set whether or not you want the weekends selectable
		datePicker.setWeekendSelectable(true);

		// Set the Date Format
		datePicker.setDateFormat(DateFormatter.DATE_FORMAT_MMDDYYYY);

		mv = (HippoDate) topic.getSingleMetaValueFor(this);

		System.out.println("MV was : " + mv);

		if (mv != null) {


			datePicker.setSelectedDate(mv.getStartDate());

		} else {
			mv = new HippoDate();
		}

		datePicker.addChangeListener(new ChangeListener() {
			public void onChange(final Widget sender) {
				SimpleDatePicker dp = (SimpleDatePicker) sender;
				System.out.println("cur " + dp.getCurrentDate());
				System.out.println("dp " + dp.getText());
				System.out.println("dp " + dp.getSelectedDate());

				DeferredCommand.add(new Command() {

					public void execute() {
						SimpleDatePicker dp = (SimpleDatePicker) sender;
						Date cDate = dp.getSelectedDate();
						// String val = cDate.getTime()+"";

						mv.setStartDate(cDate);

						// mv.setTitle(df.format(cDate));
						// mv.setData(val);

						// topicService.getTopicIdentForNameOrCreateNew(linkTo, callback)

						System.out.println("Go to Save MV " + mv.getId() + " " + mv.getStartDate());

						HippoDate hdate = new HippoDate();
						hdate.setStartDate(cDate);

						topicService.executeCommand(topic, new SaveMetaDateCommand(topic,
								MetaDate.this, hdate), new StdAsyncCallback(ConstHolder.myConstants
								.save()) {
						});

					}
				});

			}
		});


		widget.add(new Label(getName()));
		widget.add(datePicker);

		return widget;

	}



}
