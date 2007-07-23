package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Association;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveSeeAlsoCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.EnterInfoButton;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConnectionBoard extends Gadget implements CompleteListener {

	private TopicCompleter topicCompleter;
	private TopicCache topicService;
	private Topic myTopic;

	private SeeAlsoWidget alsos;
	private Manager manager;
	private VerticalPanel refPanel;

	/**
	 * Special Gadget. Is always added, but maintains own visibility, since we don't know at
	 * loadTime whether of not we'll have references.
	 * 
	 * @param manager
	 */
	public ConnectionBoard(Manager manager) {

		super(ConstHolder.myConstants.connections());

		this.manager = manager;

		topicService = manager.getTopicCache();

		topicCompleter = new TopicCompleter(manager.getTopicCache());
		topicCompleter.setCompleteListener(this);

		alsos = new SeeAlsoWidget();

		EnterInfoButton enterInfoButton = new EnterInfoButton();
		enterInfoButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				topicCompleter.complete();
			}
		});

		VerticalPanel mainP = new VerticalPanel();

		HorizontalPanel cp = new HorizontalPanel();
		cp.add(new Label(ConstHolder.myConstants.addTo()));
		cp.add(topicCompleter);
		cp.add(enterInfoButton);

		// mainP.add(b);

		refPanel = new VerticalPanel();

		mainP.add(cp);
		mainP.add(alsos);

		mainP.add(refPanel);

		initWidget(mainP);

		addStyleName("H-AbsolutePanel");
		addStyleName("H-ConnectionBoard");

		// System.out.println("P6 "+getParent(6, b.getElement()));
		// System.out.println("P8 "+getParent(8, b.getElement()));
		// System.out.println("P10 "+getParent(10, b.getElement()));
		//		
		// DOM.setIntStyleAttribute(getParent(8, b.getElement()), "z-index", 99);

	}

	// private Element getParent(int num,Element e){
	// if(0 == num){
	// return e;
	// }else{
	// return getParent(num - 1, DOM.getParent(e));
	// }
	// }

	public int load(Topic topic) {
		myTopic = topic;

		setVisible(false);

		manager.getTopicCache().getLinksTo(topic, new StdAsyncCallback("GetLinksTo") {
			public void onSuccess(Object result) {
				super.onSuccess(result);
				List list = (List) result;

				refPanel.clear();

				if (list.size() == 0) {
					// refPanel.add(new
					// Label(ConstHolder.myConstants.references_none()));
				} else {
					refPanel.add(new HeaderLabel(ConstHolder.myConstants.references()));
					setVisible(true);
				}

				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TopicIdentifier topicIdent = (TopicIdentifier) iter.next();
					refPanel.add(new TopicLink(topicIdent));
				}

				// updateTitle(refPanel,ConstHolder.myConstants.referencesN(list.size()));
				// bar.updateTitle(AllReferencesPanel.this,ConstHolder.myConstants.all_referencesN(totalSize
				// + list.size()));
			}

		});

		Association assoc = myTopic.getSeeAlsoAssociation();

		int size = alsos.load(assoc);
		if (size > 0) {
			setVisible(true);
		}
		return size;

	}

	public void completed(TopicIdentifier topicID) {

		alsos.add(topicID);
		topicCompleter.setText("");

		topicService.executeCommand(myTopic,
				new SaveSeeAlsoCommand(myTopic, new RealTopic(topicID)), new StdAsyncCallback(
						ConstHolder.myConstants.save_async()) {
				});
	}

	/**
	 * display all the see also's as links to their topic.
	 * 
	 * @author Jeff Dwyer
	 */
	private class SeeAlsoWidget extends Composite {

		private CellPanel seeAlsoPanel;

		public SeeAlsoWidget() {
			seeAlsoPanel = new VerticalPanel();
			initWidget(seeAlsoPanel);
		}

		public int load(Association seeAlsoAssoc) {
			int size = 0;
			seeAlsoPanel.clear();
			for (Iterator iter = seeAlsoAssoc.getMembers().iterator(); iter.hasNext();) {
				Topic top = (Topic) iter.next();
				seeAlsoPanel.add(new TopicLink(top.getIdentifier()));
				size++;
			}
			return size;
		}

		public void add(TopicIdentifier to2) {
			seeAlsoPanel.add(new TopicLink(to2));
		}
	}

	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.gadgetConnections().createImage();
		b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
		return b;
	}

	// @Override
	/**
	 * Special Gadget always add, but it will maintain its own visibility since we don't know at
	 * load time whether there are references
	 */
	public boolean isOnForTopic(Topic topic) {
		return true;
	}

	// @Override
	public void onClick(Manager manager) {
		throw new UnsupportedOperationException();
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.connections();
	}

}
