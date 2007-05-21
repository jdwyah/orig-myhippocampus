package com.aavu.client.domain;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TestObj implements IsSerializable{
		
	/**
     * @gwt.typeArgs <com.aavu.client.domain.TestConn>
     */
	private Set conn = new HashSet();
	
	private TestObj foo;
	
	public TestObj(){};
	
	public void addConn(TestObj t){
		conn.add(new TestConn(this,t));
	}

	public TestObj getFoo() {
		return foo;
	}

	public void setFoo(TestObj foo) {
		this.foo = foo;
	}	
	
	
}
