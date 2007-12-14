package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;

public class GadgetManager {
	private Manager manager;

	private List<Gadget> allGadgets;

	private LinkDisplayGadget linkDisplayW;
	private ConnectionBoard connectionBoard;
	private EntryGadget entryPreview;
	private ChildrenGadget childGadget;
	// private TypeGadget typeGadget;
	private CreateTopicGadget createTopicGadget;
	private CreateHippoDateGadget createHippoDateGadget;
	private CreateHippoLocationGadget createHippoLocationGadget;


	private TagBoard tagBoard;
	private OccurrenceTopicsBoard occTopicsBoard;

	private TitleGadget titleGadget;

	// private UploadBoard uploadBoard;
	// private TimeGadget timeGadget;
	// private TextMetaGadget textMetaGadget;
	// private MapGadget mapGadget;

	// private TagPropertyPanel tagProperties;

	private boolean initted = false;
	private List<GadgetClickListener> listeners = new ArrayList<GadgetClickListener>();


	public GadgetManager(Manager manager) {
		this.manager = manager;
	}

	/**
	 * PEND MED Can't do this in CTOR because Const.constholder isn't set by then.
	 */
	public void init() {
		if (!initted) {
			allGadgets = new ArrayList<Gadget>();



			// tagProperties = new TagPropertyPanel(manager);
			entryPreview = new EntryGadget(manager);
			connectionBoard = new ConnectionBoard(manager);
			linkDisplayW = new LinkDisplayGadget(manager);
			createTopicGadget = new CreateTopicGadget(manager);
			createHippoDateGadget = new CreateHippoDateGadget(manager);
			createHippoLocationGadget = new CreateHippoLocationGadget(manager);

			childGadget = new ChildrenGadget(manager);
			// typeGadget = new TypeGadget(manager);
			titleGadget = new TitleGadget(manager);
			tagBoard = new TagBoard(manager);
			occTopicsBoard = new OccurrenceTopicsBoard(manager);


			// uploadBoard = new UploadBoard(manager);
			// timeGadget = new TimeGadget(manager);
			// textMetaGadget = new TextMetaGadget(manager);
			// mapGadget = new MapGadget(manager);

			// allGadgets.add(tagProperties);


			allGadgets.add(titleGadget);
			allGadgets.add(connectionBoard);
			allGadgets.add(childGadget);
			allGadgets.add(linkDisplayW);
			allGadgets.add(entryPreview);

			allGadgets.add(createTopicGadget);
			allGadgets.add(createHippoDateGadget);
			allGadgets.add(createHippoLocationGadget);

			// allGadgets.add(typeGadget);



			allGadgets.add(tagBoard);
			allGadgets.add(occTopicsBoard);

			// allGadgets.add(uploadBoard);
			// allGadgets.add(mapGadget);
			// allGadgets.add(timeGadget);
			// allGadgets.add(textMetaGadget);
			initted = true;
		}
	}

	public List<Gadget> getFullGadgetList() {
		init();
		return allGadgets;
	}

	/**
	 * 
	 * lngLat &&|| dateCreated are ok as null
	 * 
	 * @param gadget
	 * @param lngLat
	 * @param dateCreated
	 */
	public void fireGadgetClick(Gadget gadget, int[] lngLat, Date dateCreated) {
		for (GadgetClickListener listener : listeners) {
			listener.gadgetClicked(gadget, lngLat, dateCreated);
		}

	}

	public void addGadgetClickListener(GadgetClickListener listener) {
		listeners.add(listener);

	}

	public void load(Topic topic) {
		init();

		for (Gadget gadget : allGadgets) {

			int lastLoadSize = gadget.load(topic);
			gadget.setLastLoadSize(lastLoadSize);
		}

	}

}
