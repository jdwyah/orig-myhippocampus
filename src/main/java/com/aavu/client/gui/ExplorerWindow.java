package com.aavu.client.gui;

import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.explorer.TimeLineWrapper;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.gui.maps.BigMap;
import com.aavu.client.service.Manager;

public class ExplorerWindow extends PopupWindow {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;

	private TimeLineWrapper timeline;
	private Glossary glossary;
	private BigMap bigMap;


	public ExplorerWindow(Manager manager, final GInternalFrame frame) {
		super(frame, "Timeline", WIDTH, HEIGHT);

		timeline = new TimeLineWrapper(manager, WIDTH, HEIGHT, this);
		glossary = new Glossary(manager);
		bigMap = new BigMap(manager, null, WIDTH, HEIGHT, this);

		System.out.println("\n\n\nNew Timeline " + timeline.getOffsetWidth() + " "
				+ timeline.getOffsetHeight());


		frame.setContent(timeline);

		frame.setMinimizable(false);
		frame.setClosable(true);

		frame.setVisible(false);


		// Don't actually throw away on a close action. Just minimize, which using our desktop,
		// looks like a close to the uninitiated.
		//
		frame.setDefaultCloseOperation(GFrame.DO_NOTHING_ON_CLOSE);

		frame.addFrameListener(new GFrameAdapter() {
			public void frameClosing(GFrameEvent evt) {
				frame.minimize();
			}
		});

	}


	public void loadTimeline(Topic curTopic) {

		frame.setCaption("TimeLine for " + curTopic.getTitle());
		timeline.load(curTopic);

		frame.setContent(timeline);
	}

	public void loadGlossary(Topic curTopic) {

		frame.setCaption("Glossary for " + curTopic.getTitle());

		glossary.load(curTopic);

		frame.setContent(glossary);
	}


	public void loadGoogleMap(Topic curTopic) {

		frame.setCaption("Map for " + curTopic.getTitle());
		show();// avoid the gray!

		bigMap.load(curTopic);
		bigMap.setVisible(true);
		frame.setContent(bigMap);
	}
}
