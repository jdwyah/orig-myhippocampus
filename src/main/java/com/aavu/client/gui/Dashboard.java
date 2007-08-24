package com.aavu.client.gui;

import java.util.Map;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.gui.ext.ExternalPopup;
import com.aavu.client.gui.hierarchy.ContextMenu;
import com.aavu.client.gui.hierarchy.ViewMenu;
import com.aavu.client.gui.ocean.SpatialDisplay;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Dashboard extends SimplePanel {

	private Manager manager;

	private HorizontalPanel mainPanel;

	// 'A' -> a's
	// 'B-C' -> b's & c's
	// <String,List<Topic>>
	Map sidebarEntries = new GWTSortedMap();
	private UserWidget userW;

	private ContextMenu contextMenu;

	private ViewMenu viewMenu;


	public Dashboard(Manager _manager, SpatialDisplay display) {

		this.manager = _manager;

		mainPanel = new HorizontalPanel();

		// Button tagButton = new Button(ConstHolder.myConstants.yourTags());
		// tagButton.addClickListener(new ClickListener(){
		// public void onClick(Widget sender) {
		// manager.showTagBoard();
		// }});

		contextMenu = new ContextMenu(manager, display);

		Image addNewButton = ConstHolder.images.newTopic().createImage();
		addNewButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {

				contextMenu.show();
				System.out.println("con " + contextMenu.getOffsetHeight());
				contextMenu.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop()
						- contextMenu.getOffsetHeight());
			}
		});

		viewMenu = new ViewMenu(manager);
		Image eyeButton = ConstHolder.images.eye().createImage();
		eyeButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				viewMenu.show();
				viewMenu.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop()
						- viewMenu.getOffsetHeight());
			}
		});


		// addNewButton.addMouseListener(new TooltipListener(0, -20, ConstHolder.myConstants
		// .topic_new()));



		// ImageButton timeLine = new ImageButton(ConstHolder.myConstants.timeline_image(),40,60);
		// timeLine.addClickListener(new ClickListener(){
		//
		// public void onClick(Widget sender) {
		// manager.showTimeline();
		// }});
		// timeLine.addMouseListener(new TooltipListener(0,-20,ConstHolder.myConstants.timeline()));

		Button facebookB = new Button("FaceBook");
		facebookB.addClickListener(new ClickListener() {
			// http://api.facebook.com/login.php?api_key=d1144ae411b79109d46c6d752cd4d222&popup=true
			// http://www.myhippocampus.com/site/facebook.html?auth_token=5db53300a9cebdc59ead9ea882cb57df
			public void onClick(Widget sender) {
				String login = "http://api.facebook.com/login.php?api_key=d1144ae411b79109d46c6d752cd4d222&popup=true";
				ExternalPopup ex = new ExternalPopup("Facebook", login, 626, 436);
			}
		});

		userW = new UserWidget(manager);

		// Image explorerButton = ConstHolder.images.bookAZ().createImage();
		// explorerButton.addClickListener(new ClickListener() {
		// public void onClick(Widget sender) {
		// manager.explore();
		// }
		// });
		// explorerButton.addMouseListener(new TooltipListener(0, -20, ConstHolder.myConstants
		// .explorer_tooltip()));



		mainPanel.add(addNewButton);
		mainPanel.add(eyeButton);
		// mainPanel.add(explorerButton);
		mainPanel.add(userW);

		// Button bB = new Button("Canvas");
		// bB.addClickListener(new ClickListener() {
		//
		// public void onClick(Widget sender) {
		//
		//
		// Canvas c = new Canvas(400, 400);
		//
		// CanvasRenderingContext2D ctx = c.getContext2D();
		//
		// ctx.beginPath();
		// ctx.arc(75, 75, 50, 0, (float) Math.PI * 2, true); // Outer circle
		// ctx.moveTo(110, 75);
		// ctx.arc(75, 75, 35, 0, (float) Math.PI, false); // Mouth (clockwise)
		// ctx.moveTo(65, 65);
		// ctx.arc(60, 65, 5, 0, (float) Math.PI * 2, true); // Left eye
		// ctx.moveTo(95, 65);
		// ctx.arc(90, 65, 5, 0, (float) Math.PI * 2, true); // Right eye
		// ctx.stroke();
		//
		// PopupPanel p = new PopupPanel();
		// p.add(c);
		// p.show();
		//
		// }
		// });
		// mainPanel.add(bB);

		// Button addDeliciousTags = new Button("Add Delicious Tags");
		// addDeliciousTags.addClickListener(new ClickListener() {
		// public void onClick(Widget sender) {
		// DeliciousWidget widg = new DeliciousWidget(manager);
		// }
		// });
		//
		// mainPanel.add(addDeliciousTags);

		// Button b = new Button("test");
		// b.addClickListener(new ClickListener(){
		//
		// public void onClick(Widget arg0) {
		// manager.getTopicCache().test(new StdAsyncCallback(" "){
		// public void onSuccess(Object result) {
		// super.onSuccess(result);
		// System.out.println("received "+result);
		// }});
		// }});
		// mainPanel.add(b);


		// mainPanel.add(new Label("New Topic"));
		// mainPanel.add(new Label("New Island"));
		// mainPanel.add(new Label("Explore"));
		// mainPanel.add(new TextBox());


		add(mainPanel);
		// sets
		addStyleName("H-AbsolutePanel");
		addStyleName("H-Dashboard");
	}


	public void load() {
		userW.load();
	}


}
