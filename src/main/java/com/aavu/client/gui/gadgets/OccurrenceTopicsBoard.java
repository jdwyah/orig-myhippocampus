package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveOccurrenceCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.AddButton;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.DeletableTopicLabel;
import com.aavu.client.widget.edit.RemoveListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * VERY similar to the TagBoard, but this is for Occurrence, so we save to their
 * TopicOccurrenceConnector instead of tagging.
 * 
 * @author Jeff Dwyer
 * 
 */
public class OccurrenceTopicsBoard extends Gadget implements CompleteListener, RemoveListener {

	private CellPanel tagPanel = new HorizontalPanel();

	// private List tags = new ArrayList();
	// private Map metaMap = new HashMap();

	private Occurrence cur_topic;

	private Manager manager;

	private TopicCompleter suggestBox;

	private HorizontalPanel adderP;

	private List allTopics = new ArrayList();



	// private SaveNeededListener saveNeeded;

	public OccurrenceTopicsBoard(Manager manager) {
		super(manager);
		this.manager = manager;

		// this.saveNeeded = saveNeeded;

		// final TopicCompleteOracle oracle = new
		// TopicCompleteOracle(manager.getTopicCache());

		suggestBox = new TopicCompleter(manager.getTopicCache());
		suggestBox.setCompleteListener(this);

		Image addTagButton = ConstHolder.images.enterInfo().createImage();
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
		System.out.println("occ topic board load " + (topic instanceof Occurrence));
		if (!(topic instanceof Occurrence)) {
			return -1;
		}

		allTopics.clear();

		Occurrence occ = (Occurrence) topic;

		adderP.setVisible(false);


		tagPanel.clear();

		cur_topic = occ;

		int rtnSize = 0;
		for (Iterator iter = occ.getTopicsAsTopics().iterator(); iter.hasNext();) {

			// TODO o will not be a Tag when we are a topic that is another
			// topic's MetaTopic value. capiche?
			// TODO make getTags deal with that.

			// TODO TAG
			Object o = iter.next();

			Topic tag = (Topic) o;
			allTopics.add(tag);
			showTag(tag);
			rtnSize++;

		}
		System.out.println("rtn size " + rtnSize);

		return rtnSize + 1;
	}

	private void showTag(final Topic tag) {

		// StationaryBubble topicBubble = new StationaryBubble(new FullTopicIdentifier(tag),
		// manager);
		//
		// tagPanel.add(topicBubble);

		DeletableTopicLabel tagLabel = new DeletableTopicLabel(tag, this);


		tagPanel.add(tagLabel);
	}

	/**
	 * Remove the tag and add the tag to the list of things that need to be saved. Need to do a load
	 * to make sure that we have all necessary Data.
	 */
	public void remove(Topic tag, final Widget widgetToRemoveOnSuccess) {


		if (allTopics.size() < 2) {
			manager.displayInfo("Can't remove the last tag for a topic.");
			return;
		}
		allTopics.remove(tag);

		save(widgetToRemoveOnSuccess);
	}

	private void save(final Widget widgetToRemoveOnSuccess) {

		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(cur_topic, allTopics);

		System.out.println("all topics " + allTopics.size() + " comm " + comm.getTopics().size());

		for (Iterator iterator = allTopics.iterator(); iterator.hasNext();) {
			Topic t = (Topic) iterator.next();
			System.out.println("t " + t);
		}



		if (comm.getTopicIDs().size() < 2) {
			Window.alert(ConstHolder.myConstants.link_please_pick());
			return;
		}
		// ConstHolder.myConstants.save()



		manager.getTopicCache().executeCommand(cur_topic, comm,
				new StdAsyncCallback(ConstHolder.myConstants.save()) {
					public void onSuccess(Object result) {
						super.onSuccess(result);

						if (widgetToRemoveOnSuccess != null) {

							// System.out.println(" toremove "
							// + GWT.getTypeName(widgetToRemoveOnSuccess));
							// System.out.println(" toremove " + widgetToRemoveOnSuccess);
							// System.out.println("---------------------");
							// for (int i = 0; i < tagPanel.getWidgetCount(); i++) {
							// Widget w = tagPanel.getWidget(i);
							// System.out.println("W: " + GWT.getTypeName(w));
							// System.out.println("W: " + w);
							// System.out.println("---------------------");
							// }

							tagPanel.remove(widgetToRemoveOnSuccess);
							// System.out.println("SUCCESS " + s + " "
							// + GWT.getTypeName(widgetToRemoveOnSuccess));
							// System.out.println("REMOVING " + widgetToRemoveOnSuccess + " from "
							// + widgetToRemoveOnSuccess.getParent());

							widgetToRemoveOnSuccess.removeFromParent();

						} else {
							// re-load the gadgets so we get the load() which adds the gadget
							// this doesn't happen until success.
							manager.getGadgetManager().load(cur_topic);
						}
					}

					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						manager.displayInfo("Problem Removing Tag " + caught);
					}
				});

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

		allTopics.add(tag);

		save(null);


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
		return topic instanceof Occurrence;
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
