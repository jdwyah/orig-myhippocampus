package com.aavu.server.dao;

import java.io.File;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

import junit.framework.TestCase;

public abstract class DAOTest extends TestCase {

	private static final String FILENAME = "db2.yac";
	
	protected ObjectContainer db;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("delete");
		
		new File("db.yac").delete();
		
		System.out.println("open db");		
		db=Db4o.openFile(FILENAME);	
	}
	@Override
	protected void tearDown() throws Exception {		
		super.tearDown();
		System.out.println("tear down");
		db.close();		
	}
}
