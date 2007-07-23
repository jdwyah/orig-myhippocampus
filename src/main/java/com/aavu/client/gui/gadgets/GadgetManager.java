package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;

public class GadgetManager {
	private Manager manager;

	private List allGadgets;

	private LinkDisplayWidget linkDisplayW;
	private ConnectionBoard connectionBoard;
	private EntryPreview entryPreview;
	private CreateTopicGadget createTopicGadget;

	// private UploadBoard uploadBoard;
	// private TimeGadget timeGadget;
	// private TextMetaGadget textMetaGadget;
	// private MapGadget mapGadget;

	// private TagPropertyPanel tagProperties;

	private boolean initted = false;
	private List listeners = new ArrayList();


	public GadgetManager(Manager manager) {
		this.manager = manager;
	}

	/**
	 * PEND MED Can't do this in CTOR because Const.constholder isn't set by then.
	 */
	public void init() {
		if (!initted) {
			allGadgets = new ArrayList();

			// tagProperties = new TagPropertyPanel(manager);
			entryPreview = new EntryPreview(manager);
			connectionBoard = new ConnectionBoard(manager);
			linkDisplayW = new LinkDisplayWidget(manager);
			createTopicGadget = new CreateTopicGadget(manager);

			// uploadBoard = new UploadBoard(manager);
			// timeGadget = new TimeGadget(manager);
			// textMetaGadget = new TextMetaGadget(manager);
			// mapGadget = new MapGadget(manager);

			// allGadgets.add(tagProperties);


			allGadgets.add(linkDisplayW);
			allGadgets.add(connectionBoard);
			allGadgets.add(entryPreview);
			allGadgets.add(createTopicGadget);

			// allGadgets.add(uploadBoard);
			// allGadgets.add(mapGadget);
			// allGadgets.add(timeGadget);
			// allGadgets.add(textMetaGadget);
			initted = true;
		}
	}

	public List getFullGadgetList() {
		init();
		return allGadgets;
	}

	public void fireGadgetClick(Gadget gadget, int[] lngLat) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			GadgetClickListener listener = (GadgetClickListener) iterator.next();
			listener.clicked(gadget, lngLat);
		}

	}

	public void addGadgetClickListener(GadgetClickListener listener) {
		listeners.add(listener);

	}

	public void load(Topic topic) {
		init();
		List gadgetsToUse = new ArrayList();

		for (Iterator iter = allGadgets.iterator(); iter.hasNext();) {
			Gadget gadget = (Gadget) iter.next();
			if (gadget.isOnForTopic(topic)) {
				gadgetsToUse.add(gadget);
			}
		}

		// TODO ?
		// if(displayer != null){
		// displayer.load(topic,gadgetsToUse);
		// }

	}

}
