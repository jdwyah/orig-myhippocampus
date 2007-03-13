package com.aavu.client.domain.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoBusinessException;

public abstract class CommandTestCase extends TestCase {

	protected Topic testTopic;

	@Override
	protected void setUp() throws Exception {
		
		testTopic = new Topic();
		testTopic.setId(22);
		testTopic.setTitle("Test");
	}
	
	/**
	 * PEND Would prefer to make these loads instead of gets, 
	 * but Tag's won't instanceof Tag.class when we do that.
	 * 
	 * @param command
	 * @throws HippoBusinessException 
	 */
	protected void hydrateCommand(AbstractCommand command) throws HippoBusinessException {
		
		//log.debug("Hydrate: "+command);
		
		List ids = command.getTopicIDs();
		int i = 0;
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			
			Topic t = new Topic();
			t.setId(id);
			t.setTitle("Topic "+id);
			
			
			command.setTopic(i,t);			
			
			//make sure this doesn't OOB
			System.out.println("Hydrated. "+command.getTopic(0)+" "+command.getTopic(1)+" "+command.getTopic(2));			
			
			i++;
		}
		
		
	}
	
	protected void goOverWire(AbstractCommand command) {
		command.setTopics(new ArrayList());
	}
	

	
}
