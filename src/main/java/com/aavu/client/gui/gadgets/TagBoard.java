package com.aavu.client.gui.gadgets;

import java.util.Iterator;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.RemoveTagFromTopicCommand;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.AddButton;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.DeletableTopicLabel;
import com.aavu.client.widget.edit.RemoveListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagBoard extends Gadget implements CompleteListener, RemoveListener {

	private CellPanel tagPanel = new HorizontalPanel();

	// private List tags = new ArrayList();
	// private Map metaMap = new HashMap();

	private Topic cur_topic;

	private Manager manager;

	private TopicCompleter suggestBox;

	private HorizontalPanel adderP;



	// private SaveNeededListener saveNeeded;

	public TagBoard(Manager manager) {
		super(manager);
		this.manager = manager;

		// this.saveNeeded = saveNeeded;

		// final TopicCompleteOracle oracle = new
		// TopicCompleteOracle(manager.getTopicCache());

		suggestBox = new TopicCompleter(manager.getTopicCache());
		suggestBox.setCompleteListener(this);

		EnterInfoButton addTagButton = new EnterInfoButton();
		addTagButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {

				suggestBox.complete();
			}
		});

		CellPanel mainPanel = new HorizontalPanel();


		// mainPanel.add(header);

		//		
		// CellPanel tagPanelS = new HorizontalPanel();
		// tagPanelS.add(tagPanel);

		mainPanel.add(tagPanel);

		HorizontalPanel tagBoxP = new HorizontalPanel();


		AddButton addButton = new AddButton("Add Tag");
		addButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				toggle();
			}
		});

		tagBoxP.add(addButton);

		adderP = new HorizontalPanel();
		adderP.add(new Label(ConstHolder.myConstants.addTag()));
		adderP.add(suggestBox);
		adderP.add(addTagButton);
		adderP.setVisible(false);
		tagBoxP.add(adderP);

		mainPanel.add(tagBoxP);

		initWidget(mainPanel);

		addStyleName("H-TagBoard");

	}

	public void completed(TopicIdentifier topicID) {
		Topic tag = new RealTopic();
		tag.setId(topicID.getTopicID());
		tag.setTitle(topicID.getTopicTitle());

		addTag(tag);

		suggestBox.setText("");

		adderP.setVisible(false);
	}


	private void toggle() {
		adderP.setVisible(!adderP.isVisible());
	}

	/**
	 * load the topic into the GUI
	 * 
	 * @param topic
	 */
	public int load(Topic topic) {
		adderP.setVisible(false);


		tagPanel.clear();

		cur_topic = topic;

		int rtnSize = 0;
		for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {

			// TODO o will not be a Tag when we are a topic that is another
			// topic's MetaTopic value. capiche?
			// TODO make getTags deal with that.

			// TODO TAG
			Object o = iter.next();

			Topic tag = (Topic) o;
			showTag(tag);
			rtnSize++;

		}
		return rtnSize;
	}

	private void showTag(final Topic tag) {

		// StationaryBubble topicBubble = new StationaryBubble(new FullTopicIdentifier(tag),
		// manager);
		//
		// tagPanel.add(topicBubble);

		DeletableTopicLabel tagLabel = new DeletableTopicLabel(tag, this);

		TagGadget tg = new TagGadget(tagLabel);

		tagPanel.add(tg);
	}

	/**
	 * Remove the tag and add the tag to the list of things that need to be saved. Need to do a load
	 * to make sure that we have all necessary Data.
	 */
	public void remove(Topic tag, final Widget widgetToRemoveOnSuccess) {

		if (cur_topic.getTypes().size() < 2) {
			manager.displayInfo("Can't remove the last tag for a topic.");
			return;
		}

		manager.getTopicCache().executeCommand(cur_topic,
				new RemoveTagFromTopicCommand(cur_topic, tag),
				new StdAsyncCallback(ConstHolder.myConstants.delete_async()) {
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						manager.displayInfo("Problem Removing Tag " + caught);
					}

					public void onSuccess(Object result) {
						super.onSuccess(result);
						widgetToRemoveOnSuccess.removeFromParent();
					}
				});

		// re-load the gadgets.
		manager.getGadgetManager().load(cur_topic);
	}

	/**
	 * re-load the gadgets. If we've tagged it with something with metas, we'll want to open those
	 * gadgets. ie tag Bob as Person opens Text:Email gadget.
	 * 
	 * PEND this will require a trip to server. Would be nice to make this more targetted.
	 * 
	 * @param tag
	 */
	private void addTag(final Topic tag) {

		if (cur_topic.tagTopic(tag)) {
			showTag(tag);
		}
		// incommand tagsToSave.add(tag);
		manager.getTopicCache().executeCommand(cur_topic,
				new SaveTagtoTopicCommand(cur_topic, tag),
				new StdAsyncCallback(ConstHolder.myConstants.save()) {
					// @Override
					public void onSuccess(Object result) {
						super.onSuccess(result);
						// needs a full bringUpChart, because Tag will just be a
						// placeholder
						manager.bringUpChart(cur_topic.getId());
					}
				});

	}

	// private void displayMetas(Tag tag, TagGadget tg) {
	// Set metas = tag.getTagProperties();
	//		
	// for (Iterator iter = metas.iterator(); iter.hasNext();) {
	// Meta element = (Meta) iter.next();
	//		
	// Widget w = element.getEditorWidget(cur_topic,manager);
	// w.addStyleName("H-MetaEditorWidget");
	// tg.add(w);
	//
	// }
	//
	// }

	private class TagGadget extends Composite {

		private CellPanel mainP;

		public TagGadget(DeletableTopicLabel tagLabel) {
			mainP = new VerticalPanel();

			add(tagLabel);

			initWidget(mainP);

			// addStyleName("H-Gadget");
			// addStyleName("H-TagGadget");
		}

		public void add(Widget w) {
			mainP.add(w);
		}

	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.tags();
	}

	// @Override
	public Image getPickerButton() {
		return null;
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		return !topic.getTypes().isEmpty();
	}

	// @Override
	public boolean isDisplayer() {
		return true;
	}

	// @Override
	public boolean showForIsCurrent(boolean isCurrent) {
		return true;
	}


	// @Override
	public void normalize(GadgetPopup gadgetPopup) {
		gadgetPopup.setLocation(0, 250);
	}


}
