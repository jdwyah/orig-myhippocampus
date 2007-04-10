package com.aavu.client.async;

import java.util.ArrayList;
import java.util.List;

public class NestingCallbacks {

	List nest = new ArrayList();

	/**
	 * add it to the beginning
	 * @param nestable
	 */
	public void addToNest(NestedStdAsyncCallback nestable){
		nest.add(0, nestable);
	}
	
	public void doIt(){

		System.out.println("doIt");
		NestedStdAsyncCallback cur = (NestedStdAsyncCallback) nest.get(0);
		nest.remove(0);
		cur.run(nest);

	}

}
