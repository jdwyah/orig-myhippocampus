package com.aavu.client.LinkPlugin;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.SaveOccurrenceCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.gadgets.TopicLoader;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.CompleteListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddLinkContent extends Composite implements CompleteListener {

	private WebLink link;
	private Label descRequired;
	private Label linkRequired;
	private TopicCache topicCache;

	private TextArea notesT;
	private TextBox urlT;
	private TextBox descriptionT;
	private LinksTagsBoard tagsBoard;
	private TopicLoader loader;
	private Topic myTopic;
	private CloseListener closeListener;
	private Label notificationLabel;
	private Manager manager;



	// http://localhost:8888/com.aavu.AddLink/AddLink.html?url=http%3A%2F%2Fwww.resourcebundleeditor.com%2Fwiki%2FSupport&description=ResourceBundle%20Editor&notes=pport%20mechanisms%20are%20hosted%20on%20
	// http://localhost:8888/com.aavu.AddLink/AddLink.html?url=http%3A%2F%2Fwww.google.com&description=ResourceBundle%20Editor&notes=pport%20mechanisms%20are%20hosted%20on%20

	public AddLinkContent(TopicCache topicCache, WebLink weblink, AddLinkManager manager,
			String username) {
		this(null, topicCache, weblink, null, manager, manager, false, username);
	}

	public AddLinkContent(TopicLoader widget, TopicCache _topicCache, WebLink _link,
			final Topic myTopic, final Manager manager, final CloseListener closeListener,
			boolean showDelete, String username) {
		this.topicCache = _topicCache;
		this.link = _link;
		this.loader = widget;
		this.myTopic = myTopic;
		this.closeListener = closeListener;
		this.manager = manager;

		Grid mainPanel = new Grid(6, 3);

		// mainPanel.setWidget(0,0, new HDatePicker(HorizontalPanel.ALIGN_RIGHT));

		mainPanel.setWidget(0, 0, new Label(ConstHolder.myConstants.link_description()));
		mainPanel.setWidget(1, 0, new Label(ConstHolder.myConstants.link_url()));
		mainPanel.setWidget(3, 0, new Label(ConstHolder.myConstants.link_notes()));


		/*
		 * Add a panel showing which other topics this link is associated with
		 */
		tagsBoard = new LinksTagsBoard(this, topicCache);
		tagsBoard.load(link);

		mainPanel.setWidget(4, 0, new Label(ConstHolder.myConstants.link_topics()));
		mainPanel.setWidget(4, 1, tagsBoard);


		descRequired = new Label();
		linkRequired = new Label();
		mainPanel.setWidget(0, 2, descRequired);
		mainPanel.setWidget(1, 2, linkRequired);

		descriptionT = new TextBox();
		descriptionT.setSize("35em", "2em");

		urlT = new TextBox();
		urlT.setSize("35em", "2em");

		notesT = new TextArea();
		notesT.setSize("30em", "7em");

		if (link != null) {
			if (link.getDescription() != null) {
				descriptionT.setText(link.getDescription());
			}
			if (link.getUri() != null) {
				urlT.setText(link.getUri());
			}
			if (link.getNotes() != null) {
				notesT.setText(link.getNotes());
			}
		}

		ExternalLink urlGoB = new ExternalLink(link, null, false);

		mainPanel.setWidget(0, 1, descriptionT);
		mainPanel.setWidget(1, 1, urlT);
		mainPanel.setWidget(2, 1, urlGoB);
		mainPanel.setWidget(3, 1, notesT);

		Button submitB = new Button(ConstHolder.myConstants.save());
		submitB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				submit();
			}
		});



		Button delButton = new Button(ConstHolder.myConstants.delete());
		delButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (Window.confirm(ConstHolder.myConstants.delete_occ_warning())) {
					delete();
				}
			}
		});

		if (showDelete) {
			mainPanel.setWidget(5, 1, submitB);
			mainPanel.setWidget(5, 0, delButton);
		} else {
			mainPanel.setWidget(5, 0, submitB);
		}

		notificationLabel = new Label();
		if (username != null) {
			notificationLabel.setText(ConstHolder.myConstants.link_add_foruser(username));
		}
		// notificationLabel.setVisible(true);
		mainPanel.setWidget(5, 2, notificationLabel);

		initWidget(mainPanel);
	}


	private void submit() {
		if (!prepareLink()) {
			return;
		}

		List allTopics = tagsBoard.getAllTopics();


		// int i = 0;
		// for (Iterator iter = allTopics.iterator(); iter.hasNext();) {
		// Topic t = (Topic) iter.next();
		// System.out.println("FOUND "+t+" "+i+" "+removeNumber);
		// i++;
		// }
		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(link, allTopics);
		// System.out.println("bef " + comm.getAddTopics().size() + " after "
		// + comm.getRemoveItems().size());

		System.out.println("all topics " + allTopics.size() + " comm " + comm.getTopics().size());

		for (Iterator iterator = allTopics.iterator(); iterator.hasNext();) {
			Topic t = (Topic) iterator.next();
			System.out.println("t " + t);
		}



		if (comm.getTopicIDs().size() < 2) {
			Window.alert(ConstHolder.myConstants.link_please_pick());
			return;
		}

		topicCache.executeCommand(myTopic, comm, new StdAsyncCallback(ConstHolder.myConstants
				.save()) {
			// @Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				notificationLabel.setText(ConstHolder.myConstants.link_add_fail());
				notificationLabel.setVisible(true);
			}

			// @Override
			public void onSuccess(Object result) {
				super.onSuccess(result);

				notificationLabel.setText(ConstHolder.myConstants.link_add_succ());
				notificationLabel.setVisible(true);


				if (loader != null && myTopic != null) {
					loader.load(myTopic);
				}

				Timer t = new Timer() {
					public void run() {
						closeListener.close();
					}
				};
				t.schedule(500);

			}
		});


	}

	private void delete() {
		manager.delete(link, new StdAsyncCallback(ConstHolder.myConstants.delete_occ_async()) {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				closeListener.close();
			}
		});
	}


	public void completed(TopicIdentifier topicID) {
		tagsBoard.add(topicID, new TopicLink(topicID));
		tagsBoard.clearText();
	}


	/**
	 * Create the link object.
	 * 
	 * 
	 * @param descriptionT
	 * @param urlT
	 * @param notesT
	 * @return false if required fields are not there
	 */
	private boolean prepareLink() {
		if (descriptionT.getText().length() == 0) {
			descRequired.setText(ConstHolder.myConstants.required());
			return false;
		}
		if (urlT.getText().length() == 0) {
			linkRequired.setText(ConstHolder.myConstants.required());
			return false;
		}
		link.setDescription(descriptionT.getText());
		link.setNotes(notesT.getText());
		link.setUri(urlT.getText());


		return true;
	}


}
