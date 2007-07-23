package com.aavu.client.domain.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.WebLink;
import com.aavu.client.exception.HippoException;

public class SaveOccurrenceCommandTest extends CommandTestCase {

	public void testExistingWebLink() throws HippoException {

		WebLink weblink = new WebLink();
		weblink.setId(99);
		List testTopics = new ArrayList();

		Topic t1 = new RealTopic();
		t1.setId(4);
		testTopics.add(t1);

		Topic t2 = new RealTopic();
		t2.setId(5);
		testTopics.add(t2);


		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(weblink, testTopics);

		goOverWire(comm);

		hydrateCommand(comm, true);

		comm.executeCommand();


		Topic tt = comm.getTopic(1);

		System.out.println("tt " + tt + " contains " + weblink + " "
				+ tt.getOccurenceObjs().contains(weblink));
		System.out.println("tt " + tt + " contains " + weblink + " "
				+ tt.getOccurenceObjs().contains(comm.getOccurrence()));

		assertTrue(tt.getOccurenceObjs().contains(comm.getOccurrence()));


		Topic tt2 = comm.getTopic(2);
		assertTrue(tt2.getOccurenceObjs().contains(comm.getOccurrence()));



		assertTrue(tocContains(comm.getOccurrence().getTopics(), tt));
		assertTrue(tocContains(comm.getOccurrence().getTopics(), tt2));

	}

	private boolean tocContains(Set tocs, Topic t) {
		for (Iterator iterator = tocs.iterator(); iterator.hasNext();) {
			TopicOccurrenceConnector toc = (TopicOccurrenceConnector) iterator.next();
			System.out.println("check " + toc.getTopic() + " t: " + t.getId() + " " + t.getTitle());
			System.out.println(toc.getTopic().equals(t));
			System.out.println("class " + toc.getTopic().getClass());
			if (toc.getTopic().equals(t)) {
				return true;
			}
		}
		return false;
	}

	public void test2() throws HippoException {

		WebLink weblink = new WebLink();
		List testTopics = new ArrayList();

		Topic t1 = new RealTopic();
		t1.setId(4);
		testTopics.add(t1);

		Topic t2 = new RealTopic();
		t2.setId(5);
		testTopics.add(t2);


		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(weblink, testTopics);

		goOverWire(comm);

		hydrateCommand(comm, true);

		comm.executeCommand();


		Topic tt = comm.getTopic(1);
		assertTrue(tt.getOccurenceObjs().contains(comm.getOccurrence()));


		Topic tt2 = comm.getTopic(2);
		assertTrue(tt2.getOccurenceObjs().contains(comm.getOccurrence()));


		assertTrue(tocContains(comm.getOccurrence().getTopics(), tt));
		assertTrue(tocContains(comm.getOccurrence().getTopics(), tt2));

	}

	public void test1() throws HippoException {


		WebLink weblink = new WebLink();
		List testTopics = new ArrayList();

		Topic t1 = new RealTopic();
		t1.setId(4);
		testTopics.add(t1);

		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(weblink, testTopics);

		goOverWire(comm);

		hydrateCommand(comm, true);

		comm.executeCommand();

		Topic t = comm.getTopic(1);


		assertTrue(t.getOccurenceObjs().contains(comm.getOccurrence()));

		assertTrue(tocContains(comm.getOccurrence().getTopics(), t));


	}

	public void testExistingWebLinkWithRemove() throws HippoException {

		Topic t3 = new RealTopic();
		t3.setId(8);

		WebLink weblink = new WebLink();
		weblink.setId(99);
		t3.addOccurence(weblink);

		List testTopics = new ArrayList();

		Topic t1 = new RealTopic();
		t1.setId(4);
		testTopics.add(t1);

		Topic t2 = new RealTopic();
		t2.setId(5);
		testTopics.add(t2);

		// testTopics.add(t3);
		// int removeAfter = 2;

		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(weblink, testTopics);

		// assertEquals(2, comm.getAddTopics().size());
		// assertEquals(1, comm.getRemoveItems().size());

		goOverWire(comm);

		hydrateCommand(comm, true);

		comm.executeCommand();


		Topic tt = comm.getTopic(1);
		assertTrue(tt.getOccurenceObjs().contains(comm.getOccurrence()));


		Topic tt2 = comm.getTopic(2);
		assertTrue(tt2.getOccurenceObjs().contains(comm.getOccurrence()));

		// Topic tt3 = comm.getTopic(3);
		assertFalse(t3.getOccurenceObjs().contains(comm.getOccurrence()));

		for (Iterator iterator = comm.getOccurrence().getTopics().iterator(); iterator.hasNext();) {
			TopicOccurrenceConnector toc = (TopicOccurrenceConnector) iterator.next();
			System.out.println("toc " + toc.getId() + " " + toc.getTopic() + " | "
					+ toc.getOccurrence());
		}
		System.out.println();

		assertTrue(tocContains(comm.getOccurrence().getTopics(), tt));
		assertTrue(tocContains(comm.getOccurrence().getTopics(), tt2));
		assertFalse(tocContains(comm.getOccurrence().getTopics(), t3));


		// assertFalse(comm.getOccurrence().getTopics().contains(t3));//not working bc of .equals()
	}
}
