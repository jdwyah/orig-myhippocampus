package com.aavu.client.domain.util;



import junit.framework.TestCase;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;

public class SetUtilsTest extends TestCase {
	
	
	public void testRemoveFromSetById(){
	
		Topic t = new Topic();
		
		Entry o1 = new Entry();
		o1.setId(5);
		o1.setData("foo");
		
		t.addOccurence(o1);
		
		assertEquals(1, t.getOccurenceObjs().size());
		
		SetUtils.removeFromSetById(t.getOccurenceObjs(), o1.getId());
		
		assertEquals(0, t.getOccurenceObjs().size());
		
	}
	public void testRemoveFromSetById2(){
		
		Topic t = new Topic();
		
		Entry o1 = new Entry();
		o1.setId(5);
		o1.setData("foo");			
		t.getOccurenceObjs().add(o1);
		
		Entry o2 = new Entry();
		o2.setId(6);
		o2.setData("food");
		t.getOccurenceObjs().add(o2);
		
		assertEquals(2, t.getOccurenceObjs().size());
		
		SetUtils.removeFromSetById(t.getOccurenceObjs(), o1.getId());
		
		assertEquals(1, t.getOccurenceObjs().size());
		
	}
	
}
