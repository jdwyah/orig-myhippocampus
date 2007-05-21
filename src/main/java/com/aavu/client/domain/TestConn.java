package com.aavu.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TestConn implements IsSerializable {
	
	private TestObj test1;
	private TestObj test2;
	
	public TestConn(){}
	
	public TestConn(TestObj test1, TestObj test2) {
		super();
		this.test1 = test1;
		this.test2 = test2;
	}	
}
