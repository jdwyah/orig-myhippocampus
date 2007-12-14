package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTagPropertiesCommand;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.DeleteButton;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagPropertyPanel extends Gadget {

	private VerticalPanel metaListPanel = new VerticalPanel();
	private List<Meta> metas = new ArrayList<Meta>(); // list of meta chooser objects of current
	// tag


	private Topic myTopic;

	private Button saveB;

	public TagPropertyPanel(Manager _manager) {

		super(_manager);

		VerticalPanel mainPanel = new VerticalPanel();

		mainPanel.add(metaListPanel);

		Button addB = new Button(ConstHolder.myConstants.island_property_new());
		addB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				addEditClick();
			}
		});
		saveB = new Button(ConstHolder.myConstants.island_property_save());
		saveB.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				saveProperties();
			}
		});

		HorizontalPanel bP = new HorizontalPanel();
		bP.add(addB);
		bP.add(saveB);
		mainPanel.add(bP);

		initWidget(mainPanel);

		addStyleName("H-TagProperty");
	}

	private void addEditClick() {
		manager.editMetas(new EZCallback() {
			public void onSuccess(Object result) {
				Meta m = (Meta) result;
				if (m != null) {
					addMWidg(m);
					saveB.setVisible(true);
				}
			}
		}, null);
	}

	private void addMWidg(final Meta meta) {
		final HorizontalPanel hp = new HorizontalPanel();
		final Label thisLabel = new Label(meta.getTitle());
		hp.add(thisLabel);

		DeleteButton deleteButton = new DeleteButton();
		deleteButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				metas.remove(meta);
				hp.remove(thisLabel);
			}
		});
		hp.add(deleteButton);
		metaListPanel.add(hp);

		metas.add(meta);
	}

	public int load(Topic topic) {

		setVisible(true);

		this.myTopic = topic;

		metaListPanel.clear();
		metas.clear();
		saveB.setVisible(false);

		if (myTopic.getTagProperties() != null) {
			if (myTopic.getTagProperties().size() > 0) {
				saveB.setVisible(true);
			}
			for (Meta element : myTopic.getTagProperties()) {
				addMWidg(element);
			}
		}

		return 0;
	}

	private void saveProperties() {
		// selectedTag.setTitle(tagName.getText());


		Meta[] toSave = new Meta[metas.size()];
		myTopic.getTagProperties().clear();
		int i = 0;
		for (Meta meta : metas) {

			// in command tag.addMeta(meta);
			toSave[i] = meta;
			i++;
		}
		// selectedTag.setMetas(metaChoosers);

		System.out.println("Tag: " + myTopic.getTitle());
		System.out.println("metas: " + myTopic.getTagProperties().size());
		for (Meta element : myTopic.getTagProperties()) {
			System.out.println(element.getName());
		}


		manager.getTopicCache().executeCommand(myTopic,
				new SaveTagPropertiesCommand(myTopic, toSave),
				new StdAsyncCallback<Void>("tagService saveTag") {
				});
	}


	// @Override
	public Image getPickerButton() {
		Image b = ConstHolder.images.gadgetTagProperties().createImage();
		b.addMouseListener(new TooltipListener(0, 40, getDisplayName()));
		return b;
	}

	// @Override
	public String getDisplayName() {
		return ConstHolder.myConstants.island_property();
	}

	// @Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasTagProperties();
	}


	// @Override
	public void onClick(Manager manager) {
		throw new UnsupportedOperationException();
	}

}
