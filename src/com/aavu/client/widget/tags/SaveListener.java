package com.aavu.client.widget.tags;

import java.util.Iterator;

import com.aavu.client.async.NestedStdAsyncCallback;
import com.aavu.client.async.NestingCallbacks;
import com.aavu.client.async.StdAsyncCallback;
import com.google.gwt.user.client.ui.Composite;

public abstract class SaveListener extends Composite {

	//public abstract void saveNowEvent(Iterator iter, StdAsyncCallback callback);

	public abstract void addYourNestables(NestingCallbacks nest);
	
}
