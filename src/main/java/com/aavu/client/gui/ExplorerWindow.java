package com.aavu.client.gui;

import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.connectionExplorer.ConnectionExplorer;
import com.aavu.client.gui.explorer.TimeLineWrapper;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.gui.maps.BigMap;
import com.aavu.client.service.Manager;

public class ExplorerWindow extends PopupWindow {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;



	public ExplorerWindow(Manager manager, GUIManager guiManager, final GInternalFrame frame) {
		super(frame, "Timeline", WIDTH, HEIGHT);


		// System.out.println("\n\n\nNew Timeline " + timeline.getOffsetWidth() + " "
		// + timeline.getOffsetHeight());


		// frame.setContent(guiManager.getTimeline());

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
		frame.setLocation(100, 100);

	}


	public void loadTimeline(TimeLineWrapper timeline, Topic curTopic) {

		frame.setCaption("TimeLine for " + curTopic.getTitle());
		frame.setWidth(WIDTH);
		timeline.load(curTopic);

		frame.setContent(timeline);
	}

	public void loadGlossary(Glossary glossary, Topic curTopic) {

		frame.setCaption("Glossary for " + curTopic.getTitle());
		frame.setWidth(200);
		glossary.load(curTopic);

		frame.setContent(glossary);
	}


	public void loadGoogleMap(BigMap bigMap, Topic curTopic) {

		frame.setCaption("Map for " + curTopic.getTitle());
		frame.setWidth(WIDTH);
		show();// avoid the gray!

		bigMap.load(curTopic);
		bigMap.setVisible(true);
		frame.setContent(bigMap);
	}


	public void loadConnections(ConnectionExplorer connectionExplorer, Topic curTopic) {
		frame.setCaption("Connections for " + curTopic.getTitle());
		frame.setWidth(200);
		connectionExplorer.load(curTopic);

		frame.setContent(connectionExplorer);
	}


	public int getWidth() {
		return WIDTH;
	}


	public int getHeight() {
		return HEIGHT;
	}
}
