package com.aavu.client.gui.timeline.zoomer;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.ocean.dhtmlIslands.TimelineRemembersPosition;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TimelineEditBox extends Composite {

	private static final DateTimeFormat format = DateTimeFormat.getFormat("MMM, d yyyy");

	private Manager manager;
	private EditableLabelExtension editBox;

	private TopicLink topicLink;
	private TimeLineObj tlo;
	private TimelineRemembersPosition rp;
	private HorizontalPanel topicLinkP;
	private Label dateLabel;

	public TimelineEditBox(Manager _manager) {
		super();
		this.manager = _manager;

		VerticalPanel editPanel = new VerticalPanel();
		editPanel.setStyleName("H-TimeBar-Editor");

		editBox = new EditableLabelExtension("", new ChangeListener() {
			public void onChange(Widget sender) {
				Topic fauxTopic = new RealTopic();

				fauxTopic.setId(tlo.getHasDate().getId());
				fauxTopic.setTitle(tlo.getHasDate().getTitle());

				manager.getTopicCache().executeCommand(fauxTopic,
						new SaveTitleCommand(fauxTopic, editBox.getText()),
						new StdAsyncCallback(ConstHolder.myConstants.save()) {
							// @Override
							public void onSuccess(Object result) {
								super.onSuccess(result);
								tlo.getHasDate().setTitle(editBox.getText());
								rp.updateTitle();
								manager.getGui().showHover(tlo.getTopicIdentifier());
							}
						});
			}
		});


		topicLink = new TopicLink();

		topicLinkP = new HorizontalPanel();
		topicLinkP.add(new Label("Topic:"));
		topicLinkP.add(topicLink);

		CellPanel editP = new HorizontalPanel();
		editP.add(new Label("Title:"));
		editP.add(editBox);

		dateLabel = new Label("", false);
		CellPanel dateP = new HorizontalPanel();
		editP.add(new Label("Date:"));
		editP.add(dateLabel);

		editPanel.add(editP);
		editPanel.add(topicLinkP);
		editPanel.add(dateP);

		initWidget(editPanel);
		setVisible(false);
	}

	public void setTopicAndVisible(TimelineRemembersPosition rp) {
		this.tlo = rp.getTLO();
		this.rp = rp;

		// TODO instanceof
		if (tlo.getHasDate() instanceof HippoDate) {
			topicLink.load(tlo.getTopicIdentifier());
			editBox.setText(tlo.getHasDate().getTitle());
			topicLinkP.setVisible(true);
		} else {
			topicLink.load(tlo.getTopicIdentifier());
			editBox.setText(tlo.getHasDate().getTitle());
			topicLinkP.setVisible(false);
		}
		dateLabel.setText(format.format(tlo.getStartDate()));

		setVisible(true);
	}
}
