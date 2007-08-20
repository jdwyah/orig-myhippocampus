package com.aavu.client.widget.edit;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnThisIslandBoard extends Composite implements CompleteListener {

	private static final int MAX_TO_SHOW = 6;

	private TopicCache topicService;
	private TopicCompleter topicCompleter;
	private Topic myTag;
	private Manager manager;
	private VerticalPanel onThisIslandPanel;

	private List topics;

	public OnThisIslandBoard(Manager _manager) {
		this.manager = _manager;

		topicService = manager.getTopicCache();

		topicCompleter = new TopicCompleter(manager.getTopicCache());
		topicCompleter.setCompleteListener(this);

		// alsos = new SeeAlsoWidget();

		Image enterInfoButton = ConstHolder.images.enterInfo().createImage();
		enterInfoButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				topicCompleter.complete();
			}
		});

		Button examineB = new Button(ConstHolder.myConstants.explorer_onthisisland());
		examineB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				viewMembers();
			}
		});

		VerticalPanel mainP = new VerticalPanel();

		mainP.add(new HeaderLabel(ConstHolder.myConstants.island_topics_on()));

		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new Label(ConstHolder.myConstants.addTo()));
		cp.add(topicCompleter);
		cp.add(enterInfoButton);

		onThisIslandPanel = new VerticalPanel();
		mainP.add(onThisIslandPanel);
		mainP.add(cp);

		mainP.add(examineB);
		// mainP.add(alsos);

		initWidget(mainP);

		addStyleName("H-Gadget");
		addStyleName("H-OnThisIsland");
	}

	private void viewMembers() {
		manager.explore();
	}

	public int load(Topic topic) {

		// System.out.println("\n\n\nLOAD "+(topic instanceof Tag)+" "+topic.getTypes().size());

		// if(!topic.getInstances().isEmpty()){
		setVisible(true);

		this.myTag = (Topic) topic;

		manager.getTopicCache().getTopicsWithTag(myTag.getId(),
				new StdAsyncCallback(ConstHolder.myConstants.tag_topicIsA()) {
					public void onSuccess(Object result) {
						super.onSuccess(result);
						List topics = (List) result;

						System.out.println("Show Topics results " + topics.size());

						addTopicLabels(topics);
					}
				});
		// }
		// else{
		// setVisible(false);
		// }
		return 0;
	}

	protected void addTopicLabels(List topics) {
		this.topics = topics;
		onThisIslandPanel.clear();

		int i = 0;
		for (Iterator iter = topics.iterator(); iter.hasNext();) {
			FullTopicIdentifier fti = (FullTopicIdentifier) iter.next();

			onThisIslandPanel.add(new TopicLink(fti, null));

			if (i >= MAX_TO_SHOW) {
				Label l = new Label(ConstHolder.myConstants.on_this_island_more(topics.size()
						- MAX_TO_SHOW));
				l.addClickListener(new ClickListener() {
					public void onClick(Widget sender) {
						viewMembers();
					}
				});
				l.addStyleName("gwt-Hyperlink");
				onThisIslandPanel.add(l);
				break;
			}
			i++;
		}
	}

	/**
	 * 
	 * 
	 * Then tag and save it.
	 */

	public void completed(TopicIdentifier topicID) {

		Topic newTopic = new RealTopic();
		newTopic.setTitle(topicID.getTopicTitle());
		newTopic.setId(topicID.getTopicID());

		topicCompleter.setText("");

		onThisIslandPanel.insert(new TopicLink(newTopic), 0);

		topicService.executeCommand(newTopic, new SaveTagtoTopicCommand(newTopic, myTag),
				new StdAsyncCallback(ConstHolder.myConstants.save_async()) {
				});

	}

}
