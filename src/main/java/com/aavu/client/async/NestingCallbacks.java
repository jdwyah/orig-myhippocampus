package com.aavu.client.async;

import java.util.ArrayList;
import java.util.List;

public class NestingCallbacks {

	private List<NestedStdAsyncCallback> nest = new ArrayList<NestedStdAsyncCallback>();

	/**
	 * add it to the beginning
	 * 
	 * @param nestable
	 */
	public void addToNest(NestedStdAsyncCallback nestable) {
		nest.add(0, nestable);
	}

	public void doIt() {

		System.out.println("doIt");
		NestedStdAsyncCallback cur = nest.get(0);
		nest.remove(0);
		cur.run(nest);

	}

}
