package com.aavu.client.domain.commands;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.exception.HippoBusinessException;

public class SaveOccurrenceCommandTest extends CommandTestCase {

	public void testExistingWebLink() throws HippoBusinessException{
		
		WebLink weblink = new WebLink();
		weblink.setId(99);
		List testTopics = new ArrayList();
		
		Topic t1 = new Topic();
		t1.setId(4);
		testTopics.add(t1);
		
		Topic t2 = new Topic();
		t2.setId(5);
		testTopics.add(t2);
		
		
		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(testTopics,weblink);
		
		goOverWire(comm);
		
		hydrateCommand(comm);
		
		comm.executeCommand();
		

		Topic tt = comm.getTopic(0);
		assertTrue(tt.getOccurenceObjs().contains(weblink));
		

		Topic tt2 = comm.getTopic(1);
		assertTrue(tt2.getOccurenceObjs().contains(weblink));
		
		assertTrue(comm.getOccurrence().getTopics().contains(tt));
		assertTrue(comm.getOccurrence().getTopics().contains(tt2));
		
	}
	
	public void test2() throws HippoBusinessException{
		
		WebLink weblink = new WebLink();
		List testTopics = new ArrayList();
		
		Topic t1 = new Topic();
		t1.setId(4);
		testTopics.add(t1);
		
		Topic t2 = new Topic();
		t2.setId(5);
		testTopics.add(t2);
		
		
		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(testTopics,weblink);
		
		goOverWire(comm);
		
		hydrateCommand(comm);
		
		comm.executeCommand();
		

		Topic tt = comm.getTopic(0);
		assertTrue(tt.getOccurenceObjs().contains(weblink));
		

		Topic tt2 = comm.getTopic(1);
		assertTrue(tt2.getOccurenceObjs().contains(weblink));
		
		assertTrue(comm.getOccurrence().getTopics().contains(tt));
		assertTrue(comm.getOccurrence().getTopics().contains(tt2));
		
	}
	public void test1() throws HippoBusinessException{
		
		
		WebLink weblink = new WebLink();
		List testTopics = new ArrayList();
		
		Topic t1 = new Topic();
		t1.setId(4);
		testTopics.add(t1);
				
		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(testTopics,weblink);
		
		goOverWire(comm);
		
		hydrateCommand(comm);
		
		comm.executeCommand();
		
		Topic t = comm.getTopic(0);
		assertTrue(t.getOccurenceObjs().contains(weblink));
		
		assertTrue(comm.getOccurrence().getTopics().contains(t));
		
	}
	
	public void testExistingWebLinkWithRemove() throws HippoBusinessException{
		
		Topic t3 = new Topic();
		t3.setId(8);
		
		WebLink weblink = new WebLink();
		weblink.setId(99);
		t3.addOccurence(weblink);
		
		List testTopics = new ArrayList();
		
		Topic t1 = new Topic();
		t1.setId(4);
		testTopics.add(t1);
		
		Topic t2 = new Topic();
		t2.setId(5);
		testTopics.add(t2);
				
		testTopics.add(t3);
		int removeAfter = 2;
		
		SaveOccurrenceCommand comm = new SaveOccurrenceCommand(testTopics,weblink,removeAfter);
		
		assertEquals(2, comm.getAddTopics().size());
		assertEquals(1, comm.getRemoveItems().size());
		
		goOverWire(comm);
		
		hydrateCommand(comm);
		
		comm.executeCommand();
		

		Topic tt = comm.getTopic(0);
		assertTrue(tt.getOccurenceObjs().contains(weblink));
		

		Topic tt2 = comm.getTopic(1);
		assertTrue(tt2.getOccurenceObjs().contains(weblink));
		
		Topic tt3 = comm.getTopic(2);
		assertFalse(tt3.getOccurenceObjs().contains(weblink));
		
		assertTrue(comm.getOccurrence().getTopics().contains(t1));
		assertTrue(comm.getOccurrence().getTopics().contains(t2));
		assertFalse(comm.getOccurrence().getTopics().contains(tt3));
		//assertFalse(comm.getOccurrence().getTopics().contains(t3));//not working bc of .equals()
	}
	
}
